import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IntroductionComponent } from './list/introduction.component';
import { IntroductionDetailComponent } from './detail/introduction-detail.component';
import { IntroductionUpdateComponent } from './update/introduction-update.component';
import { IntroductionDeleteDialogComponent } from './delete/introduction-delete-dialog.component';
import { IntroductionRoutingModule } from './route/introduction-routing.module';

@NgModule({
  imports: [SharedModule, IntroductionRoutingModule],
  declarations: [IntroductionComponent, IntroductionDetailComponent, IntroductionUpdateComponent, IntroductionDeleteDialogComponent],
  entryComponents: [IntroductionDeleteDialogComponent],
})
export class IntroductionModule {}
