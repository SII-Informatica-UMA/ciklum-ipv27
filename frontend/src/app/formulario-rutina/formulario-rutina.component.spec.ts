import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormularioRutinaComponent } from './formulario-rutina.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import {EjercicioService} from '../ejercicio.service';
import {Rutina} from '../rutina'
import { of } from 'rxjs';


describe('FormularioRutinaComponent', () => {
  let component: FormularioRutinaComponent;
  let fixture: ComponentFixture<FormularioRutinaComponent>;
  let mockModal: Partial<NgbActiveModal>;
  let mockEjercicioService: Partial<EjercicioService>;

  beforeEach(waitForAsync(() => {
    mockModal = {
      close: jasmine.createSpy('close')
    };

    mockEjercicioService = {
      getEjercicios: jasmine.createSpy('getEjercicios').and.returnValue(
        of([
          { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] },
          { id: 2, nombre: 'Ejercicio 2', descripcion: 'Descripción 2', observaciones: 'Observaciones 2',tipo: 'Tipo 2', musculosTrabajados: 'Músculos 2', material: 'Material 2', dificultad: 'Moderado', multimedia: [] }
        ])
      )
    };

    TestBed.configureTestingModule({
      declarations: [FormularioRutinaComponent],
      imports: [FormsModule],
      providers: [
        { provide: NgbActiveModal, useValue: mockModal },
        { provide: EjercicioService, useValue: mockEjercicioService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormularioRutinaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('debe crear', () => {
    expect(component).toBeTruthy();
  });

  it('debe inicializar con campos vacíos', () => {
    expect(component.accion).toBeFalsy();
    expect(component.ejerciciosSeleccionados).toEqual([]);
    expect(component.ejer).toEqual({ ejercicio: { id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '', dificultad: '', multimedia: [] }, series: 0, repeticiones: 0, duracionMinutos: 0 });
    expect(component.rutina).toEqual({ id: 0, nombre: '', observaciones: '', descripcion: '', ejercicios: [] });
  });

  it('debe mostrar tres botones al iniciar el formulario de añadir', () => {
    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(3);
  });

  it('debe mostrar un botón por cada ejercicio que tiene una rutina al editarla', () => {
    const numEjercicios = 3;
    const ejercicios = Array.from({ length: numEjercicios }, (_, i) => ({
      series: i + 1,
      repeticiones: i + 10,
      duracionMinutos: i + 20,
      ejercicio: {
        id: i + 1,
        nombre: `Ejercicio ${i + 1}`,
        descripcion: `Descripción del ejercicio ${i + 1}`,
        observaciones: `Observaciones del ejercicio ${i + 1}`,
        tipo: `Tipo ${i + 1}`,
        musculosTrabajados: `Músculos del ejercicio ${i + 1}`,
        material: `Material del ejercicio ${i + 1}`,
        dificultad: 'Fácil',
        multimedia: []
      }
    }));
    const rutina = { id: 1, nombre: 'Rutina de prueba', observaciones: 'Observaciones de la rutina de prueba', descripcion: 'Descripción de la rutina de prueba', ejercicios };
    component.rutina = rutina;
    const buttons = fixture.nativeElement.querySelectorAll('button');
    expect(buttons.length).toBe(numEjercicios );
  });

  it('debe mostrar un botón adicional al agregar un ejercicio al editar una rutina', () => {
    const numEjercicios = 3;
    const ejercicios = Array.from({ length: numEjercicios }, (_, i) => ({
      series: i + 1,
      repeticiones: i + 10,
      duracionMinutos: i + 20,
      ejercicio: {
        id: i + 1,
        nombre: `Ejercicio ${i + 1}`,
        descripcion: `Descripción del ejercicio ${i + 1}`,
        observaciones: `Observaciones del ejercicio ${i + 1}`,
        tipo: `Tipo ${i + 1}`,
        musculosTrabajados: `Músculos del ejercicio ${i + 1}`,
        material: `Material del ejercicio ${i + 1}`,
        dificultad: 'Fácil',
        multimedia: []
      }
    }));
    const rutina = { id: 1, nombre: 'Rutina de prueba', observaciones: 'Observaciones de la rutina de prueba', descripcion: 'Descripción de la rutina de prueba', ejercicios };
    component.rutina = rutina;
    fixture.detectChanges();
    const buttonsBefore = fixture.nativeElement.querySelectorAll('button').length;
    component.ejer = {
      ejercicio: { id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '', dificultad: '', multimedia: [] },
      series: 0,
      repeticiones: 0,
      duracionMinutos: 0
    };
    component.ejerciciosSeleccionados.push(component.ejer);
    component.agregarEjercicio();
    fixture.detectChanges();
    const buttonsAfter = fixture.nativeElement.querySelectorAll('button').length;
    expect(buttonsAfter).toBe(buttonsBefore + 1);
  });
  


  it('debe eliminar un ejercicio del formulario y mostrar un botón menos', () => {
    component.ejerciciosSeleccionados = [{ ejercicio: { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] }, series: 3, repeticiones: 10, duracionMinutos: 20 }];
    const numEjerciciosBefore = component.ejerciciosSeleccionados.length;

    component.eliminarEjercicio(0);

    expect(component.ejerciciosSeleccionados.length).toBe(numEjerciciosBefore - 1);

    const buttons = fixture.nativeElement.querySelectorAll('button');
    const addButton = buttons[buttons.length - 1];
    expect(addButton.textContent.trim()).toBe('Guardar');
  });

 
  

  it('debe agregar correctamente una rutina al cerrar el modal', () => {
    component.rutina = { id: 1, nombre: 'Rutina de prueba', observaciones: 'Esta es una rutina de prueba', descripcion: 'Descripción de la rutina de prueba', ejercicios: [] };
    component.guardarRutina();
    expect(mockModal.close).toHaveBeenCalledWith(component.rutina);
  });

  it('debe editar correctamente una rutina al cerrar el modal', () => {
    const rutinaExistente: Rutina = { id: 1, nombre: 'Rutina existente', observaciones: 'Esta es una rutina existente', descripcion: 'Descripción de la rutina existente', ejercicios: [] };
    component.rutina = rutinaExistente;
    const nuevaDescripcion = 'Nueva descripción de la rutina';
    component.rutina.descripcion = nuevaDescripcion;
    component.guardarRutina();
    expect(mockModal.close).toHaveBeenCalledWith(component.rutina);
    
  });

  it('debe agregar el ejercicio seleccionado a ejerciciosSeleccionados', () => {
    component.ejer = { ejercicio: {
      id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [],
      observaciones: 'Observaciones 1'
    }, series: 3, repeticiones: 10, duracionMinutos: 20 };
    component.agregarEjercicio();
    expect(component.ejerciciosSeleccionados.length).toBe(1);
    expect(component.ejerciciosSeleccionados[0]).toEqual({ ejercicio: { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1',observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] }, series: 3, repeticiones: 10, duracionMinutos: 20 });
  });

  it('debe eliminar el ejercicio seleccionado de ejerciciosSeleccionados', () => {
    component.ejerciciosSeleccionados = [{ ejercicio: { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] }, series: 3, repeticiones: 10, duracionMinutos: 20 }];
    component.eliminarEjercicio(0);
    expect(component.ejerciciosSeleccionados.length).toBe(0);
  });

  it('debe llamar a modal.close con rutina cuando se llama a guardarRutina', () => {
    component.rutina = { id: 1, nombre: 'Rutina 1', observaciones: 'Observaciones 1', descripcion: 'Descripción 1', ejercicios: [] };
    component.guardarRutina();
    expect(mockModal.close).toHaveBeenCalledWith(component.rutina);
  });

  it('debe agregar varios ejercicios a ejerciciosSeleccionados', () => {
    component.ejer = { ejercicio: { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] }, series: 3, repeticiones: 10, duracionMinutos: 20 };
    component.agregarEjercicio();
    component.ejer = { ejercicio: { id: 2, nombre: 'Ejercicio 2', descripcion: 'Descripción 2', observaciones: 'Observaciones 2', tipo: 'Tipo 2', musculosTrabajados: 'Músculos 2', material: 'Material 2', dificultad: 'Moderado', multimedia: [] }, series: 4, repeticiones: 15, duracionMinutos: 30 };
    component.agregarEjercicio();
    expect(component.ejerciciosSeleccionados.length).toBe(2);
  });

  it('debe eliminar varios ejercicios de ejerciciosSeleccionados', () => {
    component.ejerciciosSeleccionados = [
      { ejercicio: { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: [] }, series: 3, repeticiones: 10, duracionMinutos: 20 },
      { ejercicio: { id: 2, nombre: 'Ejercicio 2', descripcion: 'Descripción 2', observaciones: 'Observaciones 2', tipo: 'Tipo 2', musculosTrabajados: 'Músculos 2', material: 'Material 2', dificultad: 'Moderado', multimedia: [] }, series: 4, repeticiones: 15, duracionMinutos: 30 }
    ];
    component.eliminarEjercicio(0);
    expect(component.ejerciciosSeleccionados.length).toBe(1);
    expect(component.ejerciciosSeleccionados[0].ejercicio.nombre).toBe('Ejercicio 2');
  });

});

