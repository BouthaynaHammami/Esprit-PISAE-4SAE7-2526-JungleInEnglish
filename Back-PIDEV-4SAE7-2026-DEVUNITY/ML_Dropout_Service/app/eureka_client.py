from __future__ import annotations

import logging
import os
import socket
from typing import Optional

try:
    import py_eureka_client.eureka_client as eureka_client
    EUREKA_AVAILABLE = True
except ImportError:
    EUREKA_AVAILABLE = False
    eureka_client = None

logger = logging.getLogger("ml_dropout_service")


def _resolve_host() -> str:
    host = os.getenv("EUREKA_INSTANCE_HOST")
    if host:
        return host
    try:
        return socket.gethostbyname(socket.gethostname())
    except OSError:
        return "localhost"


def _resolve_port(default_port: int) -> int:
    raw_port = os.getenv("EUREKA_INSTANCE_PORT")
    if raw_port and raw_port.isdigit():
        return int(raw_port)
    return default_port


async def init_eureka(app_name: str, default_port: int) -> None:
    # Skip if Eureka is not available or explicitly disabled
    if not EUREKA_AVAILABLE:
        logger.info("Eureka client not available, skipping registration")
        return

    eureka_enabled = os.getenv("EUREKA_ENABLED", "true").lower() == "true"
    if not eureka_enabled:
        logger.info("Eureka registration disabled via EUREKA_ENABLED=false")
        return

    eureka_server = os.getenv("EUREKA_SERVER", "http://localhost:8761/eureka")
    instance_host = _resolve_host()
    instance_port = _resolve_port(default_port)

    try:
        await eureka_client.init_async(
            eureka_server=eureka_server,
            app_name=app_name,
            instance_host=instance_host,
            instance_ip=instance_host,
            instance_port=instance_port,
            renewal_interval_in_secs=30,
            duration_in_secs=90,
        )
        logger.info("Registered with Eureka at %s as %s", eureka_server, app_name)
    except Exception as exc:
        logger.warning("Eureka registration failed: %s", exc)
