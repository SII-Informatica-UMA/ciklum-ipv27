import { Injectable } from '@angular/core';
import {Ejercicio } from './ejercicio';

@Injectable({
  providedIn: 'root'
})
export class EjercicioService {
  private ejercicios: Ejercicio [] = [
    {id: 1, nombre: 'Flexiones', descripcion: 'Este ejercicio consiste en levantar y bajar el cuerpo mediante el apoyo de las manos y los pies, manteniendo la espalda recta y los músculos del abdomen contraídos.', observaciones:'Ejercicio muy útil y sencillo de realizar que puede dar resultados en poco tiempo',tipo:'Pecho',musculosTrabajados:'Pectorales y tríceps', material:'Esterilla',dificultad:'Fácil', multimedia: ['https://www.youtube.com/embed/e_EKkqoHxns?si=DbPdTLHWluuHaism', 'Imagen flexiones']},
    {id: 2, nombre: 'Abdominales', descripcion: 'Eleva el torso hacia las rodillas sin levantar la espalda completamente del suelo ni levantar los pies. Ejerce fuerza en los abdominales, evitando forzar otros músculos', observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia: ['https://www.youtube.com/embed/mMieHCr-H0c?si=iw8Fzfja0xFgJ1JQ','Imagen abdominales']},
    {id: 3, nombre: 'Sentadillas', descripcion: 'Colocar los pies a la anchura de los hombros y con las puntas de los pies mirando ligeramente hacia afuera, para después ir bajando y ejecutando el ejercicio', observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia: ['https://www.youtube.com/embed/Q2TTLUxlNu4?si=ciRZ50jhj11VZGDw','Imagen sentadillas']},
    {id: 4, nombre: 'Extensión de tríceps', descripcion:'',observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia:[]},
    {id: 5, nombre: 'Curl de bíceps', descripcion:'',observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia:[]},
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