import { Component, OnInit } from '@angular/core';
import {Ejercicio } from './ejercicio';
import {Rutina } from './rutina';
import {RutinaService} from './rutina.service';
import {EjercicioService } from './ejercicio.service';
import {FormularioRutinaComponent} from './formulario-rutina/formulario-rutina.component'
import {FormularioEjercicioComponent} from './formulario-ejercicio/formulario-ejercicio.component'
import { NgbNavModule } from '@ng-bootstrap/ng-bootstrap';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
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
  

  constructor(private ejercicioService: EjercicioService, private rutinaService: RutinaService,private sanitizer: DomSanitizer,private modalService: NgbModal) { }

 

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

  
  editarEjercicio(ejercicio: Ejercicio): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Editar"
    ref.componentInstance.ejercicio = {...ejercicio};
    ref.componentInstance.multimediaSeleccionados = [...ejercicio.multimedia];
    ref.result.then((ejercicioEditado: Ejercicio) => {
      this.ejercicioService.editarEjercicio(ejercicioEditado);
      this.ejercicios = this.ejercicioService.getEjercicios();
    }, (reason) => {});
  }

  aniadirEjercicio(): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.ejercicio = {id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '',  dificultad: '', multimedia: [] };
    ref.result.then((ejercicio: Ejercicio) => {
      this.ejercicioService.addEjercicio(ejercicio);
      this.ejercicios = this.ejercicioService.getEjercicios();
    }, (reason) => {});
  }
  

  eliminarRutina(rutina: Rutina): void {
    this.rutinaService.eliminarRutina(rutina.id);
    this.rutinas = this.rutinaService.getRutinas();
    this.rutinaElegido = undefined;
  }

  aniadirRutina(): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.rutina = { id: 0, nombre: '', observaciones:'', descripcion:'', ejercicios: []};
    ref.result.then((rutina: Rutina) => {
      this.rutinaService.addRutina(rutina);
      this.rutinas = this.rutinaService.getRutinas();
    }, (reason) => {});

  }

  editarRutina(rutina: Rutina): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Editar"
    ref.componentInstance.rutina = {...rutina};
    ref.componentInstance.ejerciciosSeleccionados = [...rutina.ejercicios];
    ref.result.then((rutinaEditada: Rutina) => {
      this.rutinaService.editarRutina(rutinaEditada);
      this.rutinas = this.rutinaService.getRutinas();
    }, (reason) => {});
  }

  getSafeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);

  }


}

