import { Injectable } from '@angular/core';
import {Rutina } from './rutina';
import { Ejercicio } from './ejercicio';

@Injectable({
  providedIn: 'root'
})
export class RutinaService {
  private rutinas: Rutina [] = [
    {id: 1, nombre: 'Pecho, hombro y tríceps', descripcion: 'Rutina de entrenamiento dirigida a aquellos/as que se inician en el mundo del fitness y quieran aumentar su masa muscular en el tren superior.', ejercicios: [{nombre: 'Flexiones', series: 1, repeticiones: 2, duracion: 3}, {nombre: 'Extensión de tríceps', series: 2, repeticiones: 3, duracion: 6}]},
    {id: 2, nombre: 'Espalda y bíceps', descripcion: 'Una rutina de espalda y bíceps para ganar densidad y amplitud que puedes hacer dos veces por semana.', ejercicios: [{nombre: 'Dorsales', series: 1, repeticiones: 2, duracion: 3}, {nombre: 'Curl de bíceps', series: 2, repeticiones: 3, duracion: 6}]},
    {id: 3, nombre: 'Piernas', descripcion: 'Trabaja y desarrolla tus piernas y glúteos con este entrenamiento de cinco ejercicios que combina rangos de fuerza e hipertrofia.', ejercicios: [{nombre: 'Sentadillas', series: 1, repeticiones: 2, duracion: 3}, {nombre: 'Prensa', series: 2, repeticiones: 3, duracion: 6}]},
  ];

  constructor() { }

  getRutinas(): Rutina [] {
    return this.rutinas;
  }


  addRutina(rutina: Rutina) {
    rutina.id = Math.max(...this.rutinas.map(c => c.id)) + 1;
    this.rutinas.push(rutina);
  }

  editarRutina(rutina: Rutina) {
    let indice = this.rutinas.findIndex(c => c.id == rutina.id);
    this.rutinas[indice] = rutina;
  }

  eliminarRutina(id: number) {
    let indice = this.rutinas.findIndex(c => c.id == id);
    this.rutinas.splice(indice, 1);
  }
}