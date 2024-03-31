import { Component } from '@angular/core';
import  {Rutina, Ejs} from '../rutina';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';



@Component({
  selector: 'app-formulario-rutina',
  templateUrl: './formulario-rutina.component.html',
  styleUrls: ['./formulario-rutina.component.css']
})
export class FormularioRutinaComponent {
  accion?: "Añadir" | "Editar";
  ejer: Ejs = { nombre: '', series: 0, repeticiones: 0, duracion: 0};
  rutina: Rutina = { id: 0, nombre: '', descripcion:'', ejercicios: []};

  constructor(public modal: NgbActiveModal) { }

  guardarRutina(): void {
    this.modal.close(this.rutina);
  }

}
