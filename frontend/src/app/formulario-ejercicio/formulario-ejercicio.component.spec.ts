import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { FormularioEjercicioComponent } from './formulario-ejercicio.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

describe('FormularioEjercicioComponent', () => {
  let component: FormularioEjercicioComponent;
  let fixture: ComponentFixture<FormularioEjercicioComponent>;
  let mockModal: Partial<NgbActiveModal>;

  beforeEach(waitForAsync(() => {
    mockModal = {
      close: jasmine.createSpy('close')
    };

    TestBed.configureTestingModule({
      declarations: [FormularioEjercicioComponent],
      imports: [FormsModule],
      providers: [
        { provide: NgbActiveModal, useValue: mockModal },
        
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(FormularioEjercicioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('debe crear', () => {
    expect(component).toBeTruthy();
  });

  it('debe inicializar con campos vacíos', () => {
    expect(component.accion).toBeFalsy();
    expect(component.ejercicio).toEqual({ id: 0, nombre: '', descripcion: '', observaciones: '', tipo: '', musculosTrabajados: '', material: '', dificultad: '', multimedia: [] });
    expect(component.multimediaSeleccionados).toEqual([]);
    expect(component.enlaceMultimedia).toBe('');
  });

  it('debe agregar un enlace multimedia a multimediaSeleccionados y ejercicio.multimedia', () => {
    component.enlaceMultimedia = 'https://example.com/image.jpg';
    component.agregarMultimedia();
    expect(component.multimediaSeleccionados.length).toBe(1);
    expect(component.ejercicio.multimedia.length).toBe(1);
    expect(component.multimediaSeleccionados[0]).toBe('https://example.com/image.jpg');
    expect(component.ejercicio.multimedia[0]).toBe('https://example.com/image.jpg');
  });

  it('debe eliminar un enlace multimedia de multimediaSeleccionados y ejercicio.multimedia', () => {
    component.multimediaSeleccionados = ['https://example.com/image1.jpg', 'https://example.com/image2.jpg'];
    component.ejercicio.multimedia = ['https://example.com/image1.jpg', 'https://example.com/image2.jpg'];
    component.eliminarMultimedia(0);
    expect(component.multimediaSeleccionados.length).toBe(1);
    expect(component.ejercicio.multimedia.length).toBe(1);
    expect(component.multimediaSeleccionados[0]).toBe('https://example.com/image2.jpg');
    expect(component.ejercicio.multimedia[0]).toBe('https://example.com/image2.jpg');
  });

  it('debe llamar a modal.close con ejercicio cuando se llama a guardarEjercicio', () => {
    const ejercicio = { id: 1, nombre: 'Ejercicio 1', descripcion: 'Descripción 1', observaciones: 'Observaciones 1', tipo: 'Tipo 1', musculosTrabajados: 'Músculos 1', material: 'Material 1', dificultad: 'Fácil', multimedia: ['https://example.com/image.jpg'] };
    component.ejercicio = ejercicio;
    component.guardarEjercicio();
    expect(mockModal.close).toHaveBeenCalledWith(ejercicio);
  });

  it('debe añadir varios enlaces multimedia a multimediaSeleccionados y ejercicio.multimedia', () => {
    component.enlaceMultimedia = 'https://example.com/image1.jpg';
    component.agregarMultimedia();
    component.enlaceMultimedia = 'https://example.com/image2.jpg';
    component.agregarMultimedia();
    expect(component.multimediaSeleccionados.length).toBe(2);
    expect(component.ejercicio.multimedia.length).toBe(2);
    expect(component.multimediaSeleccionados).toEqual(['https://example.com/image1.jpg', 'https://example.com/image2.jpg']);
    expect(component.ejercicio.multimedia).toEqual(['https://example.com/image1.jpg', 'https://example.com/image2.jpg']);
  });

  it('debe eliminar varios enlaces multimedia de multimediaSeleccionados y ejercicio.multimedia', () => {
    component.multimediaSeleccionados = ['https://example.com/image1.jpg', 'https://example.com/image2.jpg'];
    component.ejercicio.multimedia = ['https://example.com/image1.jpg', 'https://example.com/image2.jpg'];
    component.eliminarMultimedia(0);
    expect(component.multimediaSeleccionados.length).toBe(1);
    expect(component.ejercicio.multimedia.length).toBe(1);
    expect(component.multimediaSeleccionados).toEqual(['https://example.com/image2.jpg']);
    expect(component.ejercicio.multimedia).toEqual(['https://example.com/image2.jpg']);
  });
});