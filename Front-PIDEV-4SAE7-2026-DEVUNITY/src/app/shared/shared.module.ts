import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChatWindowComponent } from './components/chat-window/chat-window.component';
import { FilterPipe } from './pipes/filter.pipe';

@NgModule({
  declarations: [
    ChatWindowComponent,
    FilterPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  exports: [
    ChatWindowComponent,
    FilterPipe,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
