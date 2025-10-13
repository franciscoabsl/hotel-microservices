import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navebar } from './shared/navebar/navebar';
import { Footer } from './shared/footer/footer';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet,
    Navebar,
    Footer],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('checkin-com');
}
