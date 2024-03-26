import { Component, OnInit } from '@angular/core';
import {Ejercicio } from './ejercicio';
import {Rutina } from './rutina';
import {RutinaService} from './rutina.service';
import {EjercicioService } from './ejercicio.service';
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import { NgFor } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NgbNavModule, NgFor,NgbAccordionModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})


export class AppComponent implements OnInit{
  ejercicios: Ejercicio [] = [];
  rutinas: Rutina [] = [];
  ejercicioElegido?: Ejercicio;
  rutinaElegido?: Rutina;
  

  constructor(private ejercicioService: EjercicioService, private rutinaService: RutinaService,private sanitizer: DomSanitizer) { }

 

  ngOnInit(): void {
    this.ejercicios = this.ejercicioService.getEjercicios();
    this.rutinas = this.rutinaService.getRutinas();
    this.ejercicioElegido = this.ejercicios[0];
    this.rutinaElegido = this.rutinas[0];
  }

  elegirEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioElegido = ejercicio;
  }

  elegirRutina(rutina: Rutina): void {
    this.rutinaElegido = rutina;
  }

  eliminarEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioService.eliminarEjercicio(ejercicio.id);
    this.ejercicios = this.ejercicioService.getEjercicios();
    this.ejercicioElegido = undefined;
  }

  eliminarRutina(rutina: Rutina): void {
    this.rutinaService.eliminarRutina(rutina.id);
    this.rutinas = this.rutinaService.getRutinas();
    this.rutinaElegido = undefined;
  }

  getSafeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);

  }


}

