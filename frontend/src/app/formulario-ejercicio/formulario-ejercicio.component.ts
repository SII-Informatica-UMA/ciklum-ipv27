import { Component } from '@angular/core';
import { Ejercicio } from "../ejercicio";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component ({
    selector: 'app-formulario-ejercicio',
    templateUrl: './formulario-ejercicio.component.html',
    styleUrls: ['./formulario-ejercicio.component.css']
})
export class FormularioEjercicioComponent {
    accion?: "Añadir" | "Editar";
    ejercicio: Ejercicio = {id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '',  dificultad: '', multimedia: [] };
    multimediaSeleccionados: string[] = [];
    enlaceMultimedia: string = '';


    constructor(public modal: NgbActiveModal) { }

    guardarEjercicio(): void {
        this.ejercicio.multimedia=this.multimediaSeleccionados;
        this.modal.close(this.ejercicio);
    }

    agregarMultimedia(): void {
        this.multimediaSeleccionados.push(this.enlaceMultimedia);
        this.enlaceMultimedia = '';
    }

    eliminarMultimedia(index: number): void {
        this.multimediaSeleccionados.splice(index, 1);
      }
   
}




