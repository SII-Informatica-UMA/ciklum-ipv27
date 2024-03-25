import { Injectable } from '@angular/core';
import {Ejercicio } from './ejercicio';

@Injectable({
  providedIn: 'root'
})
export class EjercicioService {
  private ejercicios: Ejercicio [] = [
    {id: 1, nombre: 'Flexiones', descripcion: 'Este ejercicio consiste en levantar y bajar el cuerpo mediante el apoyo de las manos y los pies, manteniendo la espalda recta y los músculos del abdomen contraídos.', video: 'https://www.youtube.com/embed/e_EKkqoHxns?si=DbPdTLHWluuHaism', imagen: 'imagen flexiones'},
    {id: 2, nombre: 'Abdominales', descripcion: 'Eleva el torso hacia las rodillas sin levantar la espalda completamente del suelo ni levantar los pies. Ejerce fuerza en los abdominales, evitando forzar otros músculos', video: 'https://www.youtube.com/embed/mMieHCr-H0c?si=iw8Fzfja0xFgJ1JQ', imagen: 'imagen abdominales'},
    {id: 3, nombre: 'Sentadillas', descripcion: 'Colocar los pies a la anchura de los hombros y con las puntas de los pies mirando ligeramente hacia afuera, para después ir bajando y ejecutando el ejercicio', video: 'https://www.youtube.com/embed/Q2TTLUxlNu4?si=ciRZ50jhj11VZGDw', imagen: 'imagen sentadillas'},
  ];

  constructor() { }

  getEjercicios(): Ejercicio [] {
    return this.ejercicios;
  }

  addEjercicio(ejercicio: Ejercicio) {
    ejercicio.id = Math.max(...this.ejercicios.map(c => c.id)) + 1;
    this.ejercicios.push(ejercicio);
  }

  editarEjercicio(ejercicio: Ejercicio) {
    let indice = this.ejercicios.findIndex(c => c.id == ejercicio.id);
    this.ejercicios[indice] = ejercicio;
  }

  eliminarEjercicio(id: number) {
    let indice = this.ejercicios.findIndex(c => c.id == id);
    this.ejercicios.splice(indice, 1);
  }
}