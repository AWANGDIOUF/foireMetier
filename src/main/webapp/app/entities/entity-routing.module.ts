import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'apropos',
        data: { pageTitle: 'Apropos' },
        loadChildren: () => import('./apropos/apropos.module').then(m => m.AproposModule),
      },
      {
        path: 'introduction',
        data: { pageTitle: 'Introductions' },
        loadChildren: () => import('./introduction/introduction.module').then(m => m.IntroductionModule),
      },
      {
        path: 'objectif',
        data: { pageTitle: 'Objectifs' },
        loadChildren: () => import('./objectif/objectif.module').then(m => m.ObjectifModule),
      },
      {
        path: 'salon-metier',
        data: { pageTitle: 'SalonMetiers' },
        loadChildren: () => import('./salon-metier/salon-metier.module').then(m => m.SalonMetierModule),
      },
      {
        path: 'entreprise',
        data: { pageTitle: 'Entreprises' },
        loadChildren: () => import('./entreprise/entreprise.module').then(m => m.EntrepriseModule),
      },
      {
        path: 'evenement',
        data: { pageTitle: 'Evenements' },
        loadChildren: () => import('./evenement/evenement.module').then(m => m.EvenementModule),
      },
      {
        path: 'inscription',
        data: { pageTitle: 'Inscriptions' },
        loadChildren: () => import('./inscription/inscription.module').then(m => m.InscriptionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
