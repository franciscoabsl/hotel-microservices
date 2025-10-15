import { Component } from '@angular/core';
import { PropriedadeService } from 'src/app/core/services/propriedade/propriedade-service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { take } from 'rxjs/operators';

@Component({
  selector: 'app-propriedade',
  imports: [
    CommonModule
  ],
  templateUrl: './propriedade.html',
  styleUrl: './propriedade.scss'
})
export class Propriedade {
  dados: any[] = [];

  constructor(private propriedadeService: PropriedadeService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  carregarDados(): void {
      this.propriedadeService.getDados().subscribe({
          next: (res: any) => {
            this.dados = res
          },
          error: (err: any) => console.error('Erro ao buscar dados', err)
        });
  }

  cadastrarPropriedade(): void {
    this.router.navigate(['propriedades/cadastrar']);
  }

  editarPropriedade(propriedade: any): void {
    this.router.navigate(['propriedades', propriedade.id, 'editar']);
  }


  excluirPropriedade(id: string): void {
   if (!confirm('Tem certeza que quer excluir esta propriedade?')) return;

    console.log(`Excluir propriedade com ID: ${id}`);
    this.propriedadeService.excluir(id).pipe(take(1)).subscribe({
      next: () => {
        console.log('Exclusão realizada com sucesso');
        this.carregarDados();
      },
      error: (err) => {
        console.error('Erro ao excluir propriedade', err);
        alert('Não foi possível excluir a propriedade. Tente novamente.');
      }
    });
  }
}
