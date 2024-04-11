import { Component, OnInit } from '@angular/core';
import  {Rutina, Ejs} from '../rutina';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EjercicioService } from '../ejercicio.service';
import { Ejercicio } from '../ejercicio';




@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent implements OnInit{
  accion?: "Añadir" | "Editar";
  ejerciciosSeleccionados: Ejs[] = []; 
  ejer: Ejs = { ejercicio: {id: 0, nombre:'', descripcion:'', observaciones: '', tipo: '', musculosTrabajados:'', material: '', dificultad:'', multimedia: []}, series: 0, repeticiones: 0, duracionMinutos: 0};
  rutina: Rutina = { id: 0, nombre: '', observaciones:'', descripcion:'', ejercicios: []};
  ejerParaElegir: Ejercicio[] = [];

  constructor(public modal: NgbActiveModal, protected ejerciciosService: EjercicioService) { }

  ngOnInit(): void {
    this.ejerciciosService.getEjercicios().subscribe(data => {
      this.ejerParaElegir = data;
    });
  }

  guardarRutina(): void {
    this.rutina.ejercicios = [...this.ejerciciosSeleccionados];
    this.modal.close(this.rutina);
  }

  agregarEjercicio(): void {
    this.ejerciciosService.getEjercicios().subscribe(ejercicios => {
      const selectedExercise = ejercicios.find(e => e.id == this.ejer.ejercicio.id);
  
      if (selectedExercise) {
        this.ejerciciosSeleccionados.push({
          ejercicio: selectedExercise,
          series: this.ejer.series,
          repeticiones: this.ejer.repeticiones,
          duracionMinutos: this.ejer.duracionMinutos
        });
  
        this.ejer = {
          ejercicio: {id: 0, nombre:'', descripcion:'', observaciones: '', tipo: '', musculosTrabajados:'', material: '', dificultad:'', multimedia: []},
          series: 0,
          repeticiones: 0,
          duracionMinutos: 0
        };
      }
    });
  }
  

eliminarEjercicio(index: number): void {
  this.ejerciciosSeleccionados.splice(index, 1);
}


}
