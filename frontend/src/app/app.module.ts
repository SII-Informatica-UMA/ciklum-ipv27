import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormularioRutinaComponent } from './formulario-rutina/formulario-rutina.component';
import { FormularioEjercicioComponent } from './formulario-ejercicio/formulario-ejercicio.component';
import { HttpClientModule } from '@angular/common/http';


@NgModule({
  declarations: [
    FormularioRutinaComponent,
    FormularioEjercicioComponent
  ],
  imports: [
    BrowserModule,
    NgbModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap:[AppComponent]
})
export class AppModule { }
