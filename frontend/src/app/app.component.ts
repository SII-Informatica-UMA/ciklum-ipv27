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
  styleUrls: ['./app.component.css']
})


export class AppComponent implements OnInit{
  ejercicios: Ejercicio [] = [];
  rutinas: Rutina [] = [];
  ejercicioElegido?: Ejercicio;
  rutinaElegido?: Rutina;
  

  constructor(private ejercicioService: EjercicioService, private rutinaService: RutinaService,private sanitizer: DomSanitizer,private modalService: NgbModal) { }

 

  ngOnInit(): void {
    this.actualizaEjercicios();
    this.actualizaRutinas();
    this.ejercicioElegido = this.ejercicios[0];
    this.rutinaElegido = this.rutinas[0];
    
  }

  elegirEjercicio(ejercicio: Ejercicio): void {
    this.ejercicioElegido = ejercicio;
  }

  elegirRutina(rutina: Rutina): void {
    this.rutinaElegido = rutina;
  }

  private actualizaRutinas(id?: number): void {
    this.rutinaService.getRutinas()
      .subscribe(rutinas => {
        this.rutinas = rutinas;
        if(id){
          this.rutinaElegido = this.rutinas.find(c => c.id==id);
        }
      })
  }

  private actualizaEjercicios(id?: number): void {
    this.ejercicioService.getEjercicios()
      .subscribe(ejercicios => {
        this.ejercicios = ejercicios;
        if(id){
          this.ejercicioElegido = this.ejercicios.find(c => c.id==id);
        }
        this.ejercicios = [...this.ejercicios];
      });
  }

  eliminarEjercicio(id:number): void {
    this.ejercicioService.eliminarEjercicio(id)
      .subscribe(r => {
        this.actualizaEjercicios();
      })
    this.ejercicioElegido = undefined;
  }

  
  editarEjercicio(ejercicio: Ejercicio): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Editar"
    ref.componentInstance.ejercicio = {...ejercicio};
    ref.componentInstance.multimediaSeleccionados = [...ejercicio.multimedia];
    ref.result.then((ejercicioEditado: Ejercicio) => {
      this.ejercicioService.editarEjercicio(ejercicioEditado)
      .subscribe(c => {
        this.actualizaEjercicios(ejercicio.id);
      })
    }, (reason) => {});
   
  }

  aniadirEjercicio(): void {
    let ref = this.modalService.open(FormularioEjercicioComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.ejercicio = {id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '',  dificultad: '', multimedia: [] };
    ref.result.then((ejercicio: Ejercicio) => {
      this.ejercicioService.addEjercicio(ejercicio)
        .subscribe(c => {
          this.actualizaEjercicios();
        })
    }, (reason) => {});
   

  }
  
  eliminarRutina(id: number): void {
    this.rutinaService.eliminarRutina(id)
      .subscribe(r => {
        this.actualizaRutinas();
      });
    this.rutinaElegido = undefined;
  }

  aniadirRutina(): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.rutina = { id: 0, nombre: '', observaciones:'', descripcion:'', ejercicios: []};
    ref.result.then((rutina: Rutina) => {
      this.rutinaService.addRutina(rutina)
        .subscribe(c => {
            this.actualizaRutinas();
        })
    }, (reason) => {});

  }

  editarRutina(rutina: Rutina): void {
    let ref = this.modalService.open(FormularioRutinaComponent);
    ref.componentInstance.accion = "Editar"
    ref.componentInstance.rutina = {...rutina};
    ref.componentInstance.ejerciciosSeleccionados = [...rutina.ejercicios];
    ref.result.then((rutinaEditada: Rutina) => {
      this.rutinaService.editarRutina(rutinaEditada)
        .subscribe(c => {
          this.actualizaRutinas(rutina.id);
        })
    }, (reason) => {});
  }

  getSafeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);

  }

  /*
  ordenarEjercicios(): void {
    this.ejercicios.sort((a, b) => a.nombre.localeCompare(b.nombre));
  }

  ordenar1(ejercicio1: Ejercicio, ejercicio2: Ejercicio):number{
      if(ejercicio1.nombre < ejercicio2.nombre) return -1;
      if(ejercicio1.nombre > ejercicio2.nombre) return 1;
      return 0;
  }

  ordenarRutinas(){
    this.rutinas.sort(this.ordenar2);
  }

  ordenar2(rutina1: Rutina, rutina2: Rutina):number{
      if(rutina1.nombre < rutina2.nombre) return -1;
      if(rutina1.nombre > rutina2.nombre) return 1;
      return 0;
  }

  */


}

