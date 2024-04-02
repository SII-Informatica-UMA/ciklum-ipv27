import { Ejercicio } from "./ejercicio";

export interface Rutina {
    id: number;
    nombre: string;
    descripcion: string;
    observaciones: string;
    ejercicios: Ejs[];
  }


export interface Ejs{
    series: number;
    repeticiones: number;
    duracionMinutos: number;
    ejercicio: Ejercicio;
}