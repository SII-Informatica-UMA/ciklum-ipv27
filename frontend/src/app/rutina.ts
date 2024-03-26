import { Ejercicio } from "./ejercicio";

export interface Rutina {
    id: number;
    nombre: string;
    descripcion: string;
    ejercicios: Ejs[];
  }


export interface Ejs{
    nombre: string;
    series: number;
    repeticiones: number;
    duracion: number;
}