import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ChallengeService } from '../../../../../core/services/activity/challenge.service';
import { Challenge, ChallengeType, Level } from '../../../../../core/models/challenges-competitions.model';

/**
 * Admin Component for Challenges Management
 * 
 * Manages CRUD operations for all Challenge types.
 * Fully synchronized with Backend Spring Boot:
 * @see Activity_Management_Service/ChallengesCompetitions/Controllers/ChallengeController.java
 */
@Component({
  selector: 'app-admin-challenges',
  templateUrl: './admin-challenges.component.html',
  styleUrls: ['./admin-challenges.component.scss'],
})
export class AdminChallengesComponent implements OnInit {
  // ============ STATE ============
  loading = false;
  saving = false;
  error: string | null = null;
  successMessage: string | null = null;

  challenges: Challenge[] = [];
  searchQuery = '';

  // ============ FILTERS ============
  typeFilter: ChallengeType | '' = '';
  types: ChallengeType[] = [
    'MYSTERY_WORD',
    'SENTENCE_BUILDER',
    'EMOJI_WORD',
    'WORD_BATTLE_ROYALE',
    'STORY_CHAIN',
    'SPEED_TRANSLATION_RACE',
  ];
  levels: Level[] = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

  // ============ FORM STATE ============
  showInlineForm = false;
  selectedType: ChallengeType | null = null;
  isEdit = false;
  editingId: number | null = null;

  form: FormGroup;

  constructor(
    private challengeService: ChallengeService,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      // Common fields
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(255)]],
      description: ['', [Validators.maxLength(1000)]],
      level: [null as Level | null],

      // Timing & Attempts
      timeLimitSeconds: [null as number | null, [Validators.min(5), Validators.max(3600)]],
      maxAttempts: [null as number | null, [Validators.min(1), Validators.max(100)]],

      // Dates
      startDate: [null as string | null],
      endDate: [null as string | null],
      maxParticipants: [null as number | null, [Validators.min(2), Validators.max(1000)]],

      // MYSTERY_WORD
      hints: ['', [Validators.maxLength(1000)]],
      correctAnswer: ['', [Validators.maxLength(500)]],

      // SENTENCE_BUILDER
      scrambledSentence: ['', [Validators.maxLength(500)]],

      // EMOJI_WORD
      emojiPrompt: ['', [Validators.maxLength(100)]],

      // WORD_BATTLE_ROYALE
      wordTheme: ['', [Validators.maxLength(100)]],
      allowedLetters: ['', [Validators.maxLength(100)]],
      forbiddenWords: ['', [Validators.maxLength(500)]],

      // STORY_CHAIN
      initialSentence: ['', [Validators.maxLength(500)]],
      maxSentences: [20, [Validators.min(2), Validators.max(200)]],
      minWordsPerSentence: [5, [Validators.min(1), Validators.max(100)]],
      maxWordsPerSentence: [30, [Validators.min(2), Validators.max(200)]],
      allowVoting: [false],

      // SPEED_TRANSLATION_RACE
      sourceLanguage: ['', [Validators.maxLength(100)]],
      targetLanguage: ['', [Validators.maxLength(100)]],
      sentencesList: ['', [Validators.maxLength(5000)]],
      maxQuestions: [10, [Validators.min(1), Validators.max(100)]],
      pointsPerCorrectAnswer: [10, [Validators.min(1), Validators.max(1000)]],
      speedBonusEnabled: [false],
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  /**
   * Load all challenges from the API
   */
  loadAll(): void {
    this.loading = true;
    this.error = null;
    this.successMessage = null;

    this.challengeService.getAll().subscribe({
      next: (data) => {
        this.challenges = data ?? [];
        this.loading = false;
        this.showInlineForm = false;
      },
      error: (err) => {
        console.error('Error loading challenges:', err);
        this.loading = false;
        this.error = this.extractErrorMessage(err, 'Failed to load challenges');
      },
    });
  }

  /**
   * Compute filtered list based on search and type filter
   */
  get filtered(): Challenge[] {
    const q = this.searchQuery.trim().toLowerCase();

    return this.challenges.filter((c) => {
      const matchType = !this.typeFilter || c.type === this.typeFilter;
      const matchSearch =
        !q ||
        (c.title ?? '').toLowerCase().includes(q) ||
        (c.description ?? '').toLowerCase().includes(q);

      return matchType && matchSearch;
    });
  }

  /**
   * Reset all filters
   */
  resetFilter(): void {
    this.typeFilter = '';
    this.searchQuery = '';
  }

  /**
   * Start creating a new challenge
   */
  startCreate(): void {
    if (this.showInlineForm && !this.isEdit) {
      this.cancelForm();
      return;
    }
    this.error = null;
    this.successMessage = null;
    this.showInlineForm = true;
    this.selectedType = null;
    this.isEdit = false;
    this.editingId = null;
    this.form.reset();
  }

  /**
   * Selects a challenge type and initializes the form with defaults
   */
  pickType(t: ChallengeType): void {
    this.selectedType = t;
    this.isEdit = false;
    this.editingId = null;

    // Reset form with empty values
    this.form.reset({
      title: '',
      description: '',
      level: null,
      timeLimitSeconds: null,
      maxAttempts: null,
      hints: '',
      correctAnswer: '',
      scrambledSentence: '',
      emojiPrompt: '',
      wordTheme: '',
      allowedLetters: '',
      forbiddenWords: '',
      startDate: null,
      endDate: null,
      maxParticipants: null,
      initialSentence: '',
      maxSentences: 20,
      minWordsPerSentence: 5,
      maxWordsPerSentence: 30,
      allowVoting: false,
      sourceLanguage: '',
      targetLanguage: '',
      sentencesList: '',
      maxQuestions: 10,
      pointsPerCorrectAnswer: 10,
      speedBonusEnabled: false,
    });

    // Set type-specific defaults
    if (t === 'MYSTERY_WORD') this.form.patchValue({ maxAttempts: 5 });
    if (t === 'SENTENCE_BUILDER') this.form.patchValue({ timeLimitSeconds: 180 });
    if (t === 'WORD_BATTLE_ROYALE') this.form.patchValue({ timeLimitSeconds: 30, maxParticipants: 10 });
    if (t === 'STORY_CHAIN') this.form.patchValue({ timeLimitSeconds: 60, maxParticipants: 10 });
    if (t === 'SPEED_TRANSLATION_RACE') this.form.patchValue({ timeLimitSeconds: 60, maxParticipants: 10 });
  }

  /**
   * Load challenge data into the form for editing
   */
  onEdit(c: Challenge): void {
    if (!c.id) {
      this.error = 'Missing Challenge ID';
      return;
    }

    this.isEdit = true;
    this.editingId = c.id;
    this.selectedType = c.type;
    this.error = null;
    this.successMessage = null;
    this.showInlineForm = true;

    this.form.reset({
      title: c.title ?? '',
      description: c.description ?? '',
      level: c.level ?? null,
      timeLimitSeconds: c.timeLimitSeconds ?? null,
      maxAttempts: c.maxAttempts ?? null,
      hints: c.hints ?? '',
      correctAnswer: c.correctAnswer ?? '',
      scrambledSentence: c.scrambledSentence ?? '',
      emojiPrompt: c.emojiPrompt ?? '',
      wordTheme: c.wordTheme ?? '',
      allowedLetters: c.allowedLetters ?? '',
      forbiddenWords: c.forbiddenWords ?? '',
      startDate: c.startDate ?? null,
      endDate: c.endDate ?? null,
      maxParticipants: c.maxParticipants ?? null,
      initialSentence: c.initialSentence ?? '',
      maxSentences: c.maxSentences ?? 20,
      minWordsPerSentence: c.minWordsPerSentence ?? 5,
      maxWordsPerSentence: c.maxWordsPerSentence ?? 30,
      allowVoting: c.allowVoting ?? false,
      sourceLanguage: c.sourceLanguage ?? '',
      targetLanguage: c.targetLanguage ?? '',
      sentencesList: c.sentencesList ?? '',
      maxQuestions: c.maxQuestions ?? 10,
      pointsPerCorrectAnswer: c.pointsPerCorrectAnswer ?? 10,
      speedBonusEnabled: c.speedBonusEnabled ?? false,
    });

    // Scroll to form
    setTimeout(() => window.scrollTo({ top: 0, behavior: 'smooth' }), 50);
  }

  /**
   * Cancel form and reset to list view
   */
  cancelForm(): void {
    this.showInlineForm = false;
    this.isEdit = false;
    this.editingId = null;
    this.selectedType = null;
    this.form.reset();
    this.error = null;
  }

  /**
   * Delete a challenge after confirmation
   */
  onDelete(c: Challenge): void {
    if (!c.id) {
      this.error = 'Missing Challenge ID';
      return;
    }

    if (!confirm(`Are you sure you want to delete "${c.title}"?`)) return;

    this.loading = true;
    this.error = null;

    this.challengeService.delete(c.id).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = `"${c.title}" deleted successfully`;
        this.loadAll();
      },
      error: (err) => {
        console.error('Delete error:', err);
        this.loading = false;
        this.error = this.extractErrorMessage(err, 'Failed to delete challenge');
      },
    });
  }

  /**
   * Determine which fields to show based on selected challenge type
   */
  show(
    field:
      | 'wordTheme'
      | 'allowedLetters'
      | 'forbiddenWords'
      | 'hints'
      | 'correctAnswer'
      | 'scrambledSentence'
      | 'emojiPrompt'
  ): boolean {
    const t = this.selectedType;
    if (!t) return false;

    switch (t) {
      case 'MYSTERY_WORD':
        return field === 'hints' || field === 'correctAnswer';
      case 'SENTENCE_BUILDER':
        return field === 'scrambledSentence' || field === 'correctAnswer';
      case 'EMOJI_WORD':
        return field === 'emojiPrompt' || field === 'correctAnswer';
      case 'WORD_BATTLE_ROYALE':
        return field === 'wordTheme' || field === 'allowedLetters' || field === 'forbiddenWords';
      default:
        return false;
    }
  }

  /**
   * Sanitize form values (trim strings, convert empty to null)
   */
  private clean(v: any): any {
    if (v === undefined || v === null) return null;
    if (typeof v === 'string') {
      const trimmed = v.trim();
      return trimmed === '' ? null : trimmed;
    }
    return v;
  }

  /**
   * Build API payload based on selected challenge type
   */
  private buildPayload(): Partial<Challenge> {
    const raw = this.form.getRawValue();
    const type = this.selectedType;

    if (!type) throw new Error('Challenge type is required');

    const payload: any = {
      title: this.clean(raw.title),
      description: this.clean(raw.description),
      type,
      level: this.clean(raw.level),
      startDate: this.clean(raw.startDate),
      endDate: this.clean(raw.endDate),
      maxParticipants: this.clean(raw.maxParticipants),
    };

    // Initialize all type-specific fields to null
    payload.timeLimitSeconds = null;
    payload.maxAttempts = null;
    payload.hints = null;
    payload.correctAnswer = null;
    payload.scrambledSentence = null;
    payload.emojiPrompt = null;
    payload.wordTheme = null;
    payload.allowedLetters = null;
    payload.forbiddenWords = null;
    payload.initialSentence = null;
    payload.sourceLanguage = null;
    payload.targetLanguage = null;
    payload.sentencesList = null;

    // Build type-specific payload
    switch (type) {
      case 'MYSTERY_WORD':
        payload.maxAttempts = this.clean(raw.maxAttempts) ?? 5;
        payload.hints = this.clean(raw.hints);
        payload.correctAnswer = this.clean(raw.correctAnswer);
        break;

      case 'SENTENCE_BUILDER':
        payload.timeLimitSeconds = this.clean(raw.timeLimitSeconds) ?? 180;
        payload.scrambledSentence = this.clean(raw.scrambledSentence);
        payload.correctAnswer = this.clean(raw.correctAnswer);
        break;

      case 'EMOJI_WORD':
        payload.emojiPrompt = this.clean(raw.emojiPrompt);
        payload.correctAnswer = this.clean(raw.correctAnswer);
        break;

      case 'WORD_BATTLE_ROYALE':
        payload.wordTheme = this.clean(raw.wordTheme);
        payload.allowedLetters = this.clean(raw.allowedLetters);
        payload.forbiddenWords = this.clean(raw.forbiddenWords);
        payload.maxParticipants = this.clean(raw.maxParticipants) ?? 10;
        payload.timeLimitSeconds = this.clean(raw.timeLimitSeconds) ?? 30;
        break;

      case 'STORY_CHAIN':
        payload.initialSentence = this.clean(raw.initialSentence);
        payload.maxSentences = this.clean(raw.maxSentences) ?? 20;
        payload.minWordsPerSentence = this.clean(raw.minWordsPerSentence) ?? 5;
        payload.maxWordsPerSentence = this.clean(raw.maxWordsPerSentence) ?? 30;
        payload.allowVoting = raw.allowVoting ?? false;
        payload.maxParticipants = this.clean(raw.maxParticipants) ?? 10;
        payload.timeLimitSeconds = this.clean(raw.timeLimitSeconds) ?? 60;
        break;

      case 'SPEED_TRANSLATION_RACE':
        payload.sourceLanguage = this.clean(raw.sourceLanguage);
        payload.targetLanguage = this.clean(raw.targetLanguage);
        payload.sentencesList = this.clean(raw.sentencesList);
        payload.maxQuestions = this.clean(raw.maxQuestions) ?? 10;
        payload.pointsPerCorrectAnswer = this.clean(raw.pointsPerCorrectAnswer) ?? 10;
        payload.speedBonusEnabled = raw.speedBonusEnabled ?? false;
        payload.timeLimitSeconds = this.clean(raw.timeLimitSeconds) ?? 60;
        payload.maxParticipants = this.clean(raw.maxParticipants) ?? 10;
        break;
    }

    return payload;
  }

  /**
   * Validate dates (endDate must be >= startDate)
   */
  private validateDates(startDate: string | null, endDate: string | null): boolean {
    if (!startDate || !endDate) return true; // Optional dates
    return new Date(startDate) <= new Date(endDate);
  }

  /**
   * Save (create or update) challenge to API
   */
  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.error = 'Please fix the form errors below';
      return;
    }

    if (!this.selectedType) {
      this.error = 'Challenge type is required';
      return;
    }

    const raw = this.form.getRawValue();
    if (!this.validateDates(raw.startDate, raw.endDate)) {
      this.error = 'End date must be after or equal to start date';
      return;
    }

    this.saving = true;
    this.error = null;
    this.successMessage = null;

    let payload: Partial<Challenge>;
    try {
      payload = this.buildPayload();
    } catch (e: any) {
      this.saving = false;
      this.error = e.message || 'Failed to build payload';
      return;
    }

    const isCreating = !this.isEdit;
    const req$ = isCreating
      ? this.challengeService.create(payload)
      : this.challengeService.update(this.editingId!, payload);

    req$.subscribe({
      next: () => {
        this.saving = false;
        const action = isCreating ? 'created' : 'updated';
        this.successMessage = `Challenge ${action} successfully`;
        this.loadAll();
      },
      error: (err) => {
        console.error('Save error:', err);
        this.saving = false;
        this.error = this.extractErrorMessage(err, 'Failed to save challenge');
      },
    });
  }

  /**
   * Extract error message from HTTP error response
   */
  private extractErrorMessage(err: any, defaultMsg: string): string {
    if (err?.error?.message) return err.error.message;
    if (typeof err?.error === 'string') return err.error;
    if (err?.message) return err.message;
    return defaultMsg;
  }

  /**
   * Helper for template to filter challenges by type
   */
  filterByType(challenges: Challenge[], type: ChallengeType): Challenge[] {
    return challenges.filter(c => c.type === type);
  }

  typeLabel(t: ChallengeType): string {
    const labels: Record<ChallengeType, string> = {
      MYSTERY_WORD: 'Mystery Word',
      SENTENCE_BUILDER: 'Sentence Builder',
      EMOJI_WORD: 'Emoji Word',
      WORD_BATTLE_ROYALE: 'Word Battle Royale',
      STORY_CHAIN: 'Story Chain',
      SPEED_TRANSLATION_RACE: 'Speed Translation Race',
    };
    return labels[t] ?? t;
  }
}
