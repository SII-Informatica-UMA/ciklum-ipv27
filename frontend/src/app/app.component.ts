import { Component, OnInit } from '@angular/core';
import {Ejercicio } from './ejercicio';
import {EjercicioService } from './ejercicio.service';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { NgFor } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NgbNavModule, NgFor],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit{
  ejercicios: Ejercicio [] = [];
  ejercicioElegido?: Ejercicio;

  constructor(private ejercicioService: EjercicioService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.ejercicios = this.ejercicioService.getEjercicios();
    this.ejercicioElegido = this.ejercicios[0];
  }

  elegirEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioElegido = ejercicio;
  }

  eliminarEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioService.eliminarEjercicio(ejercicio.id);
    this.ejercicios = this.ejercicioService.getEjercicios();
    this.ejercicioElegido = undefined;
  }

  getSafeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);

  }
}
