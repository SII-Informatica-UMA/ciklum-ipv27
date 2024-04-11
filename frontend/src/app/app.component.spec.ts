import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http'; 
import { AppComponent } from './app.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { EjercicioService } from './ejercicio.service';
import { RutinaService } from './rutina.service';
import { FormularioEjercicioComponent } from './formulario-ejercicio/formulario-ejercicio.component';
import { FormularioRutinaComponent } from './formulario-rutina/formulario-rutina.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';


describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockEjercicioService: Partial<EjercicioService>;
  let mockRutinaService: Partial<RutinaService>;
  let mockModalService: Partial<NgbModal>;

  beforeEach(waitForAsync(() => {
    mockEjercicioService = {
      getEjercicios: jasmine.createSpy('getEjercicios').and.returnValue(of([])),
      eliminarEjercicio: jasmine.createSpy('eliminarEjercicio').and.returnValue(of()),
      editarEjercicio: jasmine.createSpy('editarEjercicio').and.returnValue(of()),
      addEjercicio: jasmine.createSpy('addEjercicio').and.returnValue(of())
    };

    mockRutinaService = {
      getRutinas: jasmine.createSpy('getRutinas').and.returnValue(of([])),
      eliminarRutina: jasmine.createSpy('eliminarRutina').and.returnValue(of()),
      editarRutina: jasmine.createSpy('editarRutina').and.returnValue(of()),
      addRutina: jasmine.createSpy('addRutina').and.returnValue(of())
    };

    mockModalService = {
      open: jasmine.createSpy('open').and.returnValue({
        componentInstance: {},
        result: Promise.resolve()
      })
    };

    TestBed.configureTestingModule({
      declarations: [],
      imports: [HttpClientModule, AppComponent], 
    }).compileComponents();

    /* 
        TestBed.configureTestingModule({
      declarations: [AppComponent, FormularioEjercicioComponent, FormularioRutinaComponent],
      imports: [FormsModule],
      providers: [
        { provide: EjercicioService, useValue: mockEjercicioService },
        { provide: RutinaService, useValue: mockRutinaService },
        { provide: NgbModal, useValue: mockModalService }
      ]
    }).compileComponents();*/
    
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

  }));



  it('Debería inicializarse con arrays vacíos tanto para ejercicios como para rutinas', () => {
    expect(component.ejercicios).toEqual([]);
    expect(component.rutinas).toEqual([]);
  });

  it('Debería actualizar ejercicios al inicializarse', () => {
    expect(mockEjercicioService.getEjercicios).toHaveBeenCalled();
    expect(component.ejercicios.length).toBeGreaterThan(0);
  });

  it('Debería actualizar rutinas al inicializarse', () => {
    expect(mockRutinaService.getRutinas).toHaveBeenCalled();
    expect(component.rutinas.length).toBeGreaterThan(0);
  });

  it('Debe elegir un ejercicio cuando se llama a elegirEjercicio', () => {
    const numMultimedia = 3;
    const ejercicio = {
      id: 1,
      nombre: 'Ejercicio de prueba',
      descripcion: 'Este es un ejercicio de prueba',
      observaciones: 'Observaciones del ejercicio de prueba',
      tipo: 'Tipo de prueba',
      musculosTrabajados: 'Músculos de prueba',
      material: 'Material de prueba',
      dificultad: 'Fácil',
      multimedia: Array.from({ length: numMultimedia }, (_, i) => `https://example.com/image${i + 1}.jpg`)
    };
  
    component.elegirEjercicio(ejercicio);
    expect(component.ejercicioElegido).toEqual(ejercicio);
  });

  it('Debe elegir una rutina cuando se llama a elegirRutina', () => {
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
    const rutina = { id: 1, nombre: 'Rutina de prueba', observaciones: 'Observaciones de la rutina de prueba', descripcion: 'Descripción de la rutina de prueba', ejercicios };;
    component.elegirRutina(rutina);
    expect(component.rutinaElegido).toEqual(rutina);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });
});
