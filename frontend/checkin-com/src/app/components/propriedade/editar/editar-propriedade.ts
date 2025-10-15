
import { Component } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

import { PropriedadeService } from 'src/app/core/services/propriedade/propriedade-service';
import { EstadoEnum } from 'src/app/models/estado.enum';
import { TipoPropriedadeEnum } from 'src/app/models/tipo-propriedade.enum';

@Component({
  selector: 'app-editar-propriedade',
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './editar-propriedade.html',
  styleUrls: ['./editar-propriedade.scss']
})
export class EditarPropriedade {

  estados = Object.entries(EstadoEnum).map(([sigla, nome]) => ({ sigla, nome }));
  tipos = Object.entries(TipoPropriedadeEnum).map(([sigla, nome]) => ({ sigla, nome }));
  formulario!: FormGroup;

  constructor(private propriedadeService: PropriedadeService,
    private router: Router,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.formulario = this.fb.group({
        id: ['', Validators.required],
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

     const propriedade = this.route.snapshot.data['propriedade'];

      if (propriedade) {
        this.formulario.patchValue({
          id: propriedade.id,
          nome: propriedade.nome,
          descricao: propriedade.descricao,
          tipo: propriedade.tipo,
          endereco: {
            rua: propriedade.endereco?.rua,
            bairro: propriedade.endereco?.bairro,
            cidade: propriedade.endereco?.cidade,
            estado: propriedade.endereco?.estado
          }
        });
      }
  }

  salvar() {
    if (this.formulario.valid) {
      this.propriedadeService.atualizar(this.formulario.value).subscribe({
        next: (res: any) => {
          this.router.navigate(['propriedades']);
        },
        error: (err: any) => console.error('Erro ao atualizar propriedade', err)
      });
    } else {
      console.warn('Formulário inválido!');
    }
  }

  cancelar() {
    this.router.navigate(['propriedades']);
  }
}
