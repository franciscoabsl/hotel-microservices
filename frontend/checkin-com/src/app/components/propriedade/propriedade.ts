import { Component } from '@angular/core';
import { PropriedadeService } from 'src/app/core/services/propriedade-service/propriedade-service';
import { CommonModule } from '@angular/common';
//import { CamelCasePipe } from 'src/app/pipes/camel-case-pipe';
import { Router } from '@angular/router';

@Component({
  selector: 'app-propriedade',
  imports: [
    CommonModule
    //,CamelCasePipe
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

  excluirPropriedade(id: number): void {
    console.log(`Excluir propriedade com ID: ${id}`);
  }
}
