import { Component } from '@angular/core';
import  {Rutina, Ejs} from '../rutina';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EjercicioService } from '../ejercicio.service';




@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion?: "Añadir" | "Editar";
  ejerciciosSeleccionados: Ejs[] = []; 
  ejer: Ejs = { ejercicio: {id: 0, nombre:'', descripcion:'', observaciones: '', tipo: '', musculosTrabajados:'', material: '', dificultad:'', multimedia: []}, series: 0, repeticiones: 0, duracionMinutos: 0};
  rutina: Rutina = { id: 0, nombre: '', observaciones:'', descripcion:'', ejercicios: []};

  constructor(public modal: NgbActiveModal, protected ejerciciosService: EjercicioService) { }

  guardarRutina(): void {
    console.log('guardar')
    this.rutina.ejercicios = [...this.ejerciciosSeleccionados];
    this.modal.close(this.rutina);
  }

  agregarEjercicio(): void {
    const selectedExercise = this.ejerciciosService.getEjercicios().find(e => e.id == this.ejer.ejercicio.id);

    
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

}

eliminarEjercicio(index: number): void {
  this.ejerciciosSeleccionados.splice(index, 1);
}


}
