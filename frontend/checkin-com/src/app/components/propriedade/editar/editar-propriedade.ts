import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PropriedadeService } from 'src/app/services/propriedade-service/propriedade-service';

@Component({
  selector: 'app-editar-propriedade',
  imports: [],
  templateUrl: './editar-propriedade.html',
  styleUrl: './editar-propriedade.scss'
})
export class EditarPropriedade  implements OnInit {
  propriedadeId!: string;
  propriedadeData: any;

  constructor(
   private route: ActivatedRoute,
   private propriedadeService: PropriedadeService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.propriedadeData = data['propriedade']; // nome do resolve no route config
      // montar form...
    });
  }
}
