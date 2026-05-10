import { Component, OnInit, OnDestroy, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { AiChatbotService, AiMessage } from '../../../core/services/ai-chatbot.service';

@Component({
  selector: 'app-ai-chatbot',
  templateUrl: './ai-chatbot.component.html',
  styleUrls: ['./ai-chatbot.component.css']
})
export class AiChatbotComponent implements OnInit, AfterViewChecked, OnDestroy {
  @ViewChild('messagesContainer') private messagesContainer!: ElementRef;

  isOpen = false;
  isTyping = false;
  userInput = '';
  shouldScroll = false;

  messages: { role: 'user' | 'assistant'; content: string; time: string }[] = [];
  private history: AiMessage[] = [];

  constructor(private aiService: AiChatbotService) {}

  ngOnInit(): void {
    // Show greeting after a short delay
    setTimeout(() => {
      this.addAssistantMessage(
        "👋 Hi! I'm DevUnity's AI assistant. Ask me anything about the platform, courses, or programming — I'm here to help!"
      );
    }, 500);
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollToBottom();
      this.shouldScroll = false;
    }
  }

  ngOnDestroy(): void {}

  toggleChat(): void {
    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.shouldScroll = true;
    }
  }

  sendMessage(): void {
    const text = this.userInput.trim();
    if (!text || this.isTyping) return;

    this.userInput = '';
    this.messages.push({ role: 'user', content: text, time: this.now() });
    this.history.push({ role: 'user', content: text });
    this.shouldScroll = true;
    this.isTyping = true;

    this.aiService.sendMessage(this.history, text).subscribe({
      next: (reply) => {
        this.addAssistantMessage(reply);
        this.history.push({ role: 'assistant', content: reply });
        this.isTyping = false;
      },
      error: (err) => {
        this.addAssistantMessage(err.message);
        this.isTyping = false;
      }
    });
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  private addAssistantMessage(content: string): void {
    this.messages.push({ role: 'assistant', content, time: this.now() });
    this.shouldScroll = true;
  }

  private now(): string {
    return new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }

  private scrollToBottom(): void {
    try {
      this.messagesContainer.nativeElement.scrollTop =
        this.messagesContainer.nativeElement.scrollHeight;
    } catch {}
  }
}
