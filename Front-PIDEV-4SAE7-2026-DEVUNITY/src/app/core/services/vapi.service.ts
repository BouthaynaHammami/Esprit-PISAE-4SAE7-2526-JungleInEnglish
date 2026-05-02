import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { environment } from '../../../environments/environment';

// Inline assistant config — mirrors `interviewer` from ai_mock_interviews/constants/index.ts
// This avoids relying on a pre-configured assistant in the VAPI dashboard.
// Requires: OpenAI, ElevenLabs & Deepgram keys set in your VAPI account's "Provider Keys".
const oralTestAssistant = {
  name: 'Oral Test Examiner',
  firstMessage:
    "Hello! I'm your oral examiner for today's level test. I'll ask you a few questions about the topic you just wrote about. Please speak clearly and take your time. Let's begin!",
  transcriber: {
    provider: 'deepgram',
    model: 'nova-2',
    language: 'en',
  },
  voice: {
    provider: '11labs',
    voiceId: 'sarah',
    stability: 0.4,
    similarityBoost: 0.8,
    speed: 0.9,
    style: 0.5,
    useSpeakerBoost: true,
  },
  model: {
    provider: 'openai',
    model: 'gpt-3.5-turbo',
    messages: [
      {
        role: 'system',
        content: `You are a professional English language examiner conducting a short oral test about: {{subject}}.
        Ask only 3 questions. Stay concise. 
        IMPORTANT: Wait patiently for the candidate to finish their full answer before responding or moving to the next question.`
      }
    ]
  }
};

/** Metadata for better UI display of transcripts */
export interface VapiTranscript {
  role: 'user' | 'assistant';
  text: string;
  isFinal: boolean;
}

interface VapiConfig {
  assistantId?: string;
  inlineAssistant?: any;
  variableValues?: Record<string, string>;
}

declare global {
  interface Window {
    Vapi: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class VapiService {
  private vapi: any = null;
  private isInitialized = false;
  private _isMock = false;

  private connectionSubject = new BehaviorSubject<boolean>(false);
  private speechStartSubject = new Subject<void>();
  private speechEndSubject = new Subject<void>();
  private userSpeechStartSubject = new Subject<void>();
  private userSpeechEndSubject = new Subject<void>();
  
  // ✅ IMPROVED — Unified transcript stream with role & finality
  private transcriptSubject = new Subject<VapiTranscript>();
  private callEndSubject = new Subject<void>();

  constructor() {
    this.initializeVapi();
  }

  private async initializeVapi(): Promise<void> {
    const publicKey = (environment as any).vapiPublicKey || '5830877d-b9ac-429e-a0cd-36367806a4d2';
    try {
      const { default: Vapi } = await import('@vapi-ai/web');
      this.vapi = new Vapi(publicKey);
      this._isMock = false;
      this.setupEventListeners();
      this.isInitialized = true;
      console.log('✅ VAPI loaded from npm package');
    } catch (importError) {
      console.warn('⚠️ VAPI npm import failed, using mock:', importError);
      this.createMockVapi();
    }
  }

  /** Wire events exactly as Agent.tsx / reference with extra robustness */
  private setupEventListeners(): void {
    if (!this.vapi || this._isMock) return;

    this.vapi.on('call-start', () => {
      console.log('🎤 VAPI call started');
      this.connectionSubject.next(true);
    });

    this.vapi.on('call-end', () => {
      console.log('📞 VAPI call ended');
      this.connectionSubject.next(false);
      this.callEndSubject.next();
    });

    this.vapi.on('speech-start', () => {
      this.speechStartSubject.next();
    });

    this.vapi.on('speech-end', () => {
      this.speechEndSubject.next();
    });

    // ✅ LIVE TRANSCRIPT — Catch BOTH partial & final messages like reference project wants
    this.vapi.on('message', (message: any) => {
      if (message.type === 'transcript') {
        const isFinal = message.transcriptType === 'final';
        console.log(`📝 [${message.role}] ${isFinal ? '(Final)' : '(Partial)'} ${message.transcript}`);
        
        this.transcriptSubject.next({
          role: message.role as 'user' | 'assistant',
          text: message.transcript,
          isFinal: isFinal
        });
      }
    });

    this.vapi.on('error', (error: any) => {
      console.error('❌ VAPI ERROR DETECTED:', JSON.stringify(error, null, 2));
      if (error.message) console.error('❌ Error Message:', error.message);
    });
  }

  private createMockVapi(): void {
    console.warn('🎭 Using mock VAPI — no real audio');
    this._isMock = true;
    this.isInitialized = true;
    this.vapi = {
      _isMock: true,
      start: async () => {
        setTimeout(() => this.connectionSubject.next(true), 1200);
      },
      stop: () => {
        this.connectionSubject.next(false);
        this.callEndSubject.next();
      },
      send: async (payload: any) => {
        const content: string = payload?.message?.content ?? '';
        setTimeout(() => {
          this.speechStartSubject.next();
          // Mock speech
          this.transcriptSubject.next({ role: 'assistant', text: content, isFinal: true });
          setTimeout(() => {
            this.speechEndSubject.next();
          }, 3000);
        }, 800);
      },
      on: (_e: string, _cb: Function) => {}
    };
  }

  // ─── Public API ─────────────────────────────────────────────────────────────

  async connect(config: VapiConfig): Promise<void> {
    if (!this.isInitialized || !this.vapi) {
      await this.initializeVapi();
    }

    if (this._isMock) {
      setTimeout(() => this.connectionSubject.next(true), 1000);
      return;
    }

    try {
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      stream.getTracks().forEach(t => t.stop());
    } catch {
      throw new Error('Microphone access is required. Please allow microphone and try again.');
    }

    const assistantArg = config.inlineAssistant ?? oralTestAssistant;
    await this.vapi.start(assistantArg, {
      variableValues: config.variableValues ?? {}
    });
  }

  async speak(message: string): Promise<void> {
    if (!this.vapi || this._isMock) {
      this.speechStartSubject.next();
      setTimeout(() => this.speechEndSubject.next(), message.length * 50);
      return;
    }
    await this.vapi.send({
      type: 'add-message',
      message: { role: 'system', content: message }
    });
  }

  disconnect(): void {
    this.vapi?.stop();
  }

  getInitializationStatus(): 'ready' | 'initializing' | 'failed' {
    return this.isInitialized && this.vapi ? 'ready' : 'initializing';
  }

  isRealVapi(): boolean { return this.isInitialized && !!this.vapi && !this._isMock; }
  isConnected(): boolean { return this.connectionSubject.value; }

  // ─── Observable getters ──────────────────────────────────────────────────────
  onConnectionChange(): Observable<boolean> { return this.connectionSubject.asObservable(); }
  onSpeechStart(): Observable<void> { return this.speechStartSubject.asObservable(); }
  onSpeechEnd(): Observable<void> { return this.speechEndSubject.asObservable(); }
  onUserSpeechStart(): Observable<void> { return this.userSpeechStartSubject.asObservable(); }
  onUserSpeechEnd(): Observable<void> { return this.userSpeechEndSubject.asObservable(); }
  
  onTranscript(): Observable<VapiTranscript> { return this.transcriptSubject.asObservable(); }
  onCallEnd(): Observable<void> { return this.callEndSubject.asObservable(); }
}