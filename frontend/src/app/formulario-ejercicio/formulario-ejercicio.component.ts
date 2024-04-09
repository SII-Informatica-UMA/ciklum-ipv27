import { Component } from '@angular/core';
import { Ejercicio } from "../ejercicio";
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EjercicioService } from '../ejercicio.service';

@Component ({
    selector: 'app-formulario-ejercicio',
    templateUrl: './formulario-ejercicio.component.html',
    styleUrls: ['./formulario-ejercicio.component.css']
})
export class FormularioEjercicioComponent {
    accion?: "Añadir" | "Editar";
    ejercicio: Ejercicio = {id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '',  dificultad: '', multimedia: [] };
    constructor(public modal: NgbActiveModal) { }

    guardarEjercicio(): void {
        console.log('guardar')

        this.modal.close(this.ejercicio) ;
    }
    /*
    guardarRutina(): void {
        console.log('guardar')
        this.rutina.ejercicios = [...this.ejerciciosSeleccionados];
        this.modal.close(this.rutina);
      }
      */
}




