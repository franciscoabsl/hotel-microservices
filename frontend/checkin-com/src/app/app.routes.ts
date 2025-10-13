import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { Home } from './components/home/home';
import { Reserva } from './components/reserva/reserva';
import { Propriedade } from './components/propriedade/propriedade';
import { CadastrarPropriedade } from './components/propriedade/cadastrar/cadastrar-propriedade';
import { EditarPropriedade } from './components/propriedade/editar/editar-propriedade';
import { PropriedadeResolver } from './resolvers/propriedade.resolver';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'home', component: Home },
  { path: 'reservas', component: Reserva },
  { path: 'propriedades', component: Propriedade},
  { path: 'propriedades/cadastrar', component: CadastrarPropriedade},
  {
    path: 'propriedades/:id/editar',
    component: EditarPropriedade,
    resolve: { propriedade: PropriedadeResolver }
  },
];
