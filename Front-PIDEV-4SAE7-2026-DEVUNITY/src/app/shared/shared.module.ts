import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChatWindowComponent } from './components/chat-window/chat-window.component';
import { AiChatbotComponent } from './components/ai-chatbot/ai-chatbot.component';
import { FilterPipe } from './pipes/filter.pipe';

@NgModule({
  declarations: [
    ChatWindowComponent,
    AiChatbotComponent,
    FilterPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    ChatWindowComponent,
    AiChatbotComponent,
    FilterPipe,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
