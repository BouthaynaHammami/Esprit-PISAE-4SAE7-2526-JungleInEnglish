from __future__ import annotations

from dataclasses import dataclass
from typing import Any, Dict, List, Optional

import joblib
import numpy as np
import pandas as pd


@dataclass
class ModelArtifacts:
    model: Any
    scaler: Optional[Any]
    encoders: Dict[str, Any]
    feature_order: List[str]
    label_mapping: Dict[int, str]
    threshold: Optional[float]
    model_name: str


def _normalize_encoders(raw: Optional[Dict[str, Any]]) -> Dict[str, Any]:
    if not raw:
        return {}

    normalized: Dict[str, Any] = {}
    for col, encoder in raw.items():
        if hasattr(encoder, "classes_"):
            normalized[col] = list(encoder.classes_)
        elif isinstance(encoder, (list, tuple)):
            normalized[col] = list(encoder)
        elif isinstance(encoder, dict):
            normalized[col] = encoder
    return normalized


def load_artifacts(path: str) -> ModelArtifacts:
    data = joblib.load(path)

    if isinstance(data, dict) and "model" in data:
        model = data["model"]
        scaler = data.get("scaler")
        encoders = _normalize_encoders(data.get("encoders"))
        feature_order = data.get("feature_order") or data.get("features")
        label_mapping = data.get("label_mapping") or {0: "no", 1: "yes"}
        threshold = data.get("threshold")
        model_name = data.get("model_name") or model.__class__.__name__
    else:
        model = data
        scaler = None
        encoders = {}
        feature_order = getattr(model, "feature_names_in_", None)
        label_mapping = {0: "no", 1: "yes"}
        threshold = None
        model_name = model.__class__.__name__

    if feature_order is None:
        raise ValueError(
            "feature_order is missing. Export the model with a feature_order list."
        )

    return ModelArtifacts(
        model=model,
        scaler=scaler,
        encoders=encoders,
        feature_order=list(feature_order),
        label_mapping=label_mapping,
        threshold=threshold,
        model_name=model_name,
    )


def _normalize_string(value: str) -> str:
    return "".join(ch.lower() for ch in value if ch.isalnum())


def _lookup_normalized(value: str, candidates: List[str]) -> str | None:
    normalized_value = _normalize_string(value)
    for candidate in candidates:
        if normalized_value == _normalize_string(candidate):
            return candidate
    return None


def _resolve_cefr_alias(col: str, value: str, candidates: List[str]) -> str | None:
    if col != "english_level_self":
        return None

    cefr_aliases = {
        "a1": ["beginner"],
        "a2": ["elementary"],
        "b1": ["intermediate"],
        "b2": ["upperintermediate", "upper-intermediate"],
        "c1": ["advanced"],
        "c2": ["mastery"],
    }

    normalized_value = _normalize_string(value)
    for code, aliases in cefr_aliases.items():
        if normalized_value != code:
            continue
        for alias in aliases:
            match = _lookup_normalized(alias, candidates)
            if match is not None:
                return match
    return None


def _encode_value(col: str, value: Any, encoder: Any) -> Any:
    if isinstance(encoder, dict):
        if value in encoder:
            return encoder[value]
        if isinstance(value, str):
            keys = [str(key) for key in encoder.keys()]
            match = _lookup_normalized(value, keys)
            if match is not None and match in encoder:
                return encoder[match]
            alias = _resolve_cefr_alias(col, value, keys)
            if alias is not None and alias in encoder:
                return encoder[alias]
        raise ValueError(f"Unknown value '{value}' for '{col}'.")

    if value in encoder:
        return encoder.index(value)

    if isinstance(value, str):
        string_candidates = [candidate for candidate in encoder if isinstance(candidate, str)]
        match = _lookup_normalized(value, string_candidates)
        if match is not None:
            return encoder.index(match)
        alias = _resolve_cefr_alias(col, value, string_candidates)
        if alias is not None:
            return encoder.index(alias)

        if col == "english_level_self" and isinstance(value, str):
            cefr_order = ["A1", "A2", "B1", "B2", "C1", "C2"]
            normalized = _normalize_string(value)
            try:
                index = [c.lower() for c in cefr_order].index(normalized)
                if encoder and index < len(encoder):
                    return index
            except ValueError:
                pass
        if col == "preferred_learning_mode" and isinstance(value, str):
            learning_modes = ["online", "in-person", "hybrid"]
            normalized = _normalize_string(value)
            try:
                index = [m.lower() for m in learning_modes].index(normalized)
                if encoder and index < len(encoder):
                    return index
            except ValueError:
                pass

    raise ValueError(f"Unknown value '{value}' for '{col}'.")


def prepare_features(input_data: Dict[str, Any], artifacts: ModelArtifacts) -> np.ndarray:
    row: Dict[str, Any] = {}
    for feature in artifacts.feature_order:
        if feature not in input_data:
            raise ValueError(f"Missing feature '{feature}'.")
        value = input_data[feature]
        if feature in artifacts.encoders:
            value = _encode_value(feature, value, artifacts.encoders[feature])
        row[feature] = value

    frame = pd.DataFrame([row], columns=artifacts.feature_order)
    features = frame.values
    if artifacts.scaler is not None:
        features = artifacts.scaler.transform(features)
    return features
