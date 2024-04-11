import { Injectable } from '@angular/core';
import {Rutina } from './rutina';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})

/*export class RutinaService {
  private rutinas: Rutina [] = [
    {id: 1, nombre: 'Pecho, hombro y tríceps', descripcion: 'Rutina de entrenamiento dirigida a aquellos/as que se inician en el mundo del fitness y quieran aumentar su masa muscular en el tren superior.', observaciones:'', ejercicios: [{ejercicio: {id: 1, nombre: 'Flexiones', descripcion: 'Este ejercicio consiste en levantar y bajar el cuerpo mediante el apoyo de las manos y los pies, manteniendo la espalda recta y los músculos del abdomen contraídos.', observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia: ['https://www.youtube.com/embed/e_EKkqoHxns?si=DbPdTLHWluuHaism', 'Imagen flexiones']}, series: 1, repeticiones: 2, duracionMinutos: 3}, {ejercicio: {id: 4, nombre: 'Extensión de tríceps', descripcion:'',observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia:[]}, series: 2, repeticiones: 3, duracionMinutos: 6}]},
    {id: 2, nombre: 'Espalda y bíceps', descripcion: 'Una rutina de espalda y bíceps para ganar densidad y amplitud que puedes hacer dos veces por semana.', observaciones:'', ejercicios: [{ejercicio: {id: 2, nombre: 'Abdominales', descripcion: 'Eleva el torso hacia las rodillas sin levantar la espalda completamente del suelo ni levantar los pies. Ejerce fuerza en los abdominales, evitando forzar otros músculos', observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia: ['https://www.youtube.com/embed/mMieHCr-H0c?si=iw8Fzfja0xFgJ1JQ','Imagen abdominales']}, series: 1, repeticiones: 2, duracionMinutos: 3}, {ejercicio: {id: 5, nombre: 'Curl de bíceps', descripcion:'',observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia:[]}, series: 2, repeticiones: 3, duracionMinutos: 6}]},
    {id: 3, nombre: 'Piernas', descripcion: 'Trabaja y desarrolla tus piernas y glúteos con este entrenamiento de cinco ejercicios que combina rangos de fuerza e hipertrofia.', observaciones:'', ejercicios: [{ejercicio: {id: 3, nombre: 'Sentadillas', descripcion: 'Colocar los pies a la anchura de los hombros y con las puntas de los pies mirando ligeramente hacia afuera, para después ir bajando y ejecutando el ejercicio', observaciones:'',tipo:'',musculosTrabajados:'', material:'',dificultad:'', multimedia: ['https://www.youtube.com/embed/Q2TTLUxlNu4?si=ciRZ50jhj11VZGDw','Imagen sentadillas']}, series: 1, repeticiones: 2, duracionMinutos: 3}]},
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
*/


export class RutinaService {
  private baseURI: string = 'http://localhost:8080/rutina';

  constructor(private http: HttpClient) { }

  getRutinas(): Observable<Rutina []> {
    return this.http.get<Rutina []>(this.baseURI+'?entrenador=1');
  }

  addRutina(rutina: Rutina): Observable<Rutina> {
    const{id, ...rest}=rutina;
    return this.http.post<Rutina>(this.baseURI+'?entrenador=1', rest);
  }

  editarRutina(rutina: Rutina): Observable<Rutina> {
    return this.http.put<Rutina>(this.baseURI + '/' + rutina.id, rutina);
  }

  eliminarRutina(id: number): Observable<HttpResponse<string>> {
    return this.http.delete(this.baseURI + '/' + id, {observe: "response", responseType: 'text'});
  }
}

