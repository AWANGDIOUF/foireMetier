import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SalonMetierComponent } from './list/salon-metier.component';
import { SalonMetierDetailComponent } from './detail/salon-metier-detail.component';
import { SalonMetierUpdateComponent } from './update/salon-metier-update.component';
import { SalonMetierDeleteDialogComponent } from './delete/salon-metier-delete-dialog.component';
import { SalonMetierRoutingModule } from './route/salon-metier-routing.module';

@NgModule({
  imports: [SharedModule, SalonMetierRoutingModule],
  declarations: [SalonMetierComponent, SalonMetierDetailComponent, SalonMetierUpdateComponent, SalonMetierDeleteDialogComponent],
  entryComponents: [SalonMetierDeleteDialogComponent],
})
export class SalonMetierModule {}
