import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms'; // <-- importar aqui

import { PropriedadeService } from 'src/app/core/services/propriedade-service/propriedade-service';
import { EstadoEnum } from 'src/app/models/estado.enum';
import { TipoPropriedadeEnum } from 'src/app/models/tipo-propriedade.enum';

@Component({
  selector: 'app-cadastrar-propriedade',
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './cadastrar-propriedade.html',
  styleUrl: './cadastrar-propriedade.scss'
})
export class CadastrarPropriedade {

  estados = Object.entries(EstadoEnum).map(([sigla, nome]) => ({ sigla, nome }));
  tipos = Object.entries(TipoPropriedadeEnum).map(([sigla, nome]) => ({ sigla, nome }));
  formulario!: FormGroup;

  constructor(private propriedadeService: PropriedadeService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.formulario = this.fb.group({
        nome: ['', Validators.required],
        descricao: [''],
        tipo: [''],
        endereco: this.fb.group({
          rua: ['', Validators.required],
          bairro: ['', Validators.required],
          cidade: ['', Validators.required],
          estado: ['', Validators.required]
        })
      });
  }

  salvar() {
    if (this.formulario.valid) {
      this.propriedadeService.cadastrar(this.formulario.value).subscribe({
        next: (res: any) => {
          this.router.navigate(['propriedades', res.id]);
        },
        error: (err: any) => console.error('Erro ao cadastrar propriedade', err)
      });
    } else {
      console.warn('Formulário inválido!');
    }
  }

  cancelar() {
    this.router.navigate(['propriedades']);
  }
}
