package com.demo.sp19.app.di.module;



import com.demo.architect.data.repository.base.account.remote.AccountRepository;
import com.demo.architect.data.repository.base.gift.remote.GiftRepository;
import com.demo.architect.data.repository.base.notification.remote.NotificationRepository;
import com.demo.architect.data.repository.base.other.remote.OtherRepository;
import com.demo.architect.data.repository.base.product.remote.ProductRepository;
import com.demo.architect.data.repository.base.upload.remote.UploadRepository;
import com.demo.architect.domain.AddBrandSetUsedUsecase;
import com.demo.architect.domain.AddCustomerGiftMegaUsecase;
import com.demo.architect.domain.AddCustomerGiftUsecase;
import com.demo.architect.domain.AddCustomerImageUsecase;
import com.demo.architect.domain.AddCustomerProductUsecase;
import com.demo.architect.domain.AddCustomerTotalDialMegaUsecase;
import com.demo.architect.domain.AddCustomerUsecase;
import com.demo.architect.domain.AddImageAttendanceUsecase;
import com.demo.architect.domain.AddPOSMUsecase;
import com.demo.architect.domain.AddProfileEmergencyUsecase;
import com.demo.architect.domain.AddTakeOffVolumnUsecase;
import com.demo.architect.domain.AddWarehouseRequirementSetUsecase;
import com.demo.architect.domain.AttendanceUsecase;
import com.demo.architect.domain.ChangePassUsecase;
import com.demo.architect.domain.CompleteProfileEmergencyUsecase;
import com.demo.architect.domain.ConfirmWarehouseRequirementSetUsecase;
import com.demo.architect.domain.GetBackgroundUsecase;
import com.demo.architect.domain.GetBrandSetDetailUsecase;
import com.demo.architect.domain.GetBrandSetUsecase;
import com.demo.architect.domain.GetBrandUsecase;
import com.demo.architect.domain.GetCurrentBrandSetUsecase;
import com.demo.architect.domain.GetDataLuckyMegaUsecase;
import com.demo.architect.domain.GetPOSMUsecase;
import com.demo.architect.domain.GetUpdateAppUsecase;
import com.demo.architect.domain.GetEmergencyUsecase;
import com.demo.architect.domain.GetGiftUsecase;
import com.demo.architect.domain.GetNotificationUsecase;
import com.demo.architect.domain.GetOutletBrandUsecase;
import com.demo.architect.domain.GetProductGiftUsecase;
import com.demo.architect.domain.GetProductUsecase;
import com.demo.architect.domain.GetVersionUsecase;
import com.demo.architect.domain.GetWarehouseRequirementUsecase;
import com.demo.architect.domain.LoginUsecase;
import com.demo.architect.domain.LogoutUsecase;
import com.demo.architect.domain.ReadedNotificationSetGiftMegaUsecase;
import com.demo.architect.domain.UpdateChangeSetUsecase;
import com.demo.architect.domain.UpdateCurrentGiftUsecase;
import com.demo.architect.domain.UpdateStockUsecase;
import com.demo.architect.domain.UpdateTypeUsecase;
import com.demo.architect.domain.UploadImageUsecase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by uyminhduc on 12/16/16.
 */
@Module
public class UseCaseModule {
    public UseCaseModule() {
    }

    @Provides
    LoginUsecase provideLoginUsecase(AccountRepository remoteRepository) {
        return new LoginUsecase(remoteRepository);
    }

    @Provides
    ChangePassUsecase provideChangePassUsecase(AccountRepository remoteRepository) {
        return new ChangePassUsecase(remoteRepository);
    }

    @Provides
    GetNotificationUsecase provideGetNotificationUsecase(NotificationRepository remoteRepository) {
        return new GetNotificationUsecase(remoteRepository);
    }

    @Provides
    GetUpdateAppUsecase provideGetDateUsecase(OtherRepository remoteRepository) {
        return new GetUpdateAppUsecase(remoteRepository);
    }

    @Provides
    AttendanceUsecase provideAttendanceUsecase(AccountRepository remoteRepository) {
        return new AttendanceUsecase(remoteRepository);
    }

    @Provides
    UploadImageUsecase provideUploadImageUsecase(UploadRepository remoteRepository) {
        return new UploadImageUsecase(remoteRepository);
    }

    @Provides
    AddImageAttendanceUsecase provideAddImageAttendanceUsecase(AccountRepository remoteRepository) {
        return new AddImageAttendanceUsecase(remoteRepository);
    }

    @Provides
    GetProductUsecase provideGetProductUsecase(ProductRepository remoteRepository) {
        return new GetProductUsecase(remoteRepository);
    }

    @Provides
    UpdateStockUsecase provideUpdateStockUsecase(OtherRepository remoteRepository) {
        return new UpdateStockUsecase(remoteRepository);
    }

    @Provides
    UpdateTypeUsecase provideUpdateTypeUsecase(OtherRepository remoteRepository) {
        return new UpdateTypeUsecase(remoteRepository);
    }
    @Provides
    AddTakeOffVolumnUsecase provideAddTakeOffVolumnUsecase(OtherRepository remoteRepository) {
        return new AddTakeOffVolumnUsecase(remoteRepository);
    }

    @Provides
    GetBrandSetDetailUsecase provideGetBrandSetDetailUsecase(ProductRepository remoteRepository) {
        return new GetBrandSetDetailUsecase(remoteRepository);
    }


    @Provides
    GetBrandSetUsecase provideGetBrandSetUsecase(ProductRepository remoteRepository) {
        return new GetBrandSetUsecase(remoteRepository);
    }


    @Provides
    GetBrandUsecase provideGetBrandUsecase(ProductRepository remoteRepository) {
        return new GetBrandUsecase(remoteRepository);
    }


    @Provides
    GetGiftUsecase provideGetGiftUsecase(ProductRepository remoteRepository) {
        return new GetGiftUsecase(remoteRepository);
    }

    @Provides
    GetOutletBrandUsecase provideGetOutletBrandUsecase(ProductRepository remoteRepository) {
        return new GetOutletBrandUsecase(remoteRepository);
    }

    @Provides
    GetBackgroundUsecase provideGetBackgroundUsecase(OtherRepository remoteRepository) {
        return new GetBackgroundUsecase(remoteRepository);
    }

    @Provides
    AddCustomerUsecase provideAddCustomerUsecase(GiftRepository remoteRepository) {
        return new AddCustomerUsecase(remoteRepository);
    }

    @Provides
    AddCustomerGiftUsecase provideAddCustomerGiftUsecase(GiftRepository remoteRepository) {
        return new AddCustomerGiftUsecase(remoteRepository);
    }

    @Provides
    AddCustomerProductUsecase provideAddCustomerProductUsecase(GiftRepository remoteRepository) {
        return new AddCustomerProductUsecase(remoteRepository);
    }

    @Provides
    AddCustomerImageUsecase provideAddCustomerImageUsecase(GiftRepository remoteRepository) {
        return new AddCustomerImageUsecase(remoteRepository);
    }

    @Provides
    GetCurrentBrandSetUsecase provideGetCurrentSetUsecase(GiftRepository remoteRepository) {
        return new GetCurrentBrandSetUsecase(remoteRepository);
    }

    @Provides
    GetProductGiftUsecase provideGetProductGiftUsecase(GiftRepository remoteRepository) {
        return new GetProductGiftUsecase(remoteRepository);
    }

    @Provides
    AddWarehouseRequirementSetUsecase provideAddWarehouseRequirementSetUsecase(GiftRepository remoteRepository) {
        return new AddWarehouseRequirementSetUsecase(remoteRepository);
    }

    @Provides
    UpdateCurrentGiftUsecase provideUpdateCurrentGiftUsecase(GiftRepository remoteRepository) {
        return new UpdateCurrentGiftUsecase(remoteRepository);
    }

    @Provides
    ConfirmWarehouseRequirementSetUsecase provideConfirmWarehouseRequirementSetUsecase(GiftRepository remoteRepository) {
        return new ConfirmWarehouseRequirementSetUsecase(remoteRepository);
    }

    @Provides
    UpdateChangeSetUsecase provideUpdateChangeSetUsecase(GiftRepository remoteRepository) {
        return new UpdateChangeSetUsecase(remoteRepository);
    }

    @Provides
    GetEmergencyUsecase provideGetEmergencyUsecase(OtherRepository remoteRepository) {
        return new GetEmergencyUsecase(remoteRepository);
    }

    @Provides
    AddProfileEmergencyUsecase provideAddProfileEmergencyUsecase(OtherRepository remoteRepository) {
        return new AddProfileEmergencyUsecase(remoteRepository);
    }

    @Provides
    CompleteProfileEmergencyUsecase provideCompleteProfileEmergencyUsecase(OtherRepository remoteRepository) {
        return new CompleteProfileEmergencyUsecase(remoteRepository);
    }

    @Provides
    AddBrandSetUsedUsecase provideAddBrandSetUsedUsecase(GiftRepository remoteRepository) {
        return new AddBrandSetUsedUsecase(remoteRepository);
    }

    @Provides
    GetPOSMUsecase provideGetPOSMUsecase(OtherRepository remoteRepository) {
        return new GetPOSMUsecase(remoteRepository);
    }

    @Provides
    AddPOSMUsecase provideAddPOSMUsecase(OtherRepository remoteRepository) {
        return new AddPOSMUsecase(remoteRepository);
    }

    @Provides
    GetWarehouseRequirementUsecase provideGetWarehouseRequirementUsecase(GiftRepository remoteRepository) {
        return new GetWarehouseRequirementUsecase(remoteRepository);
    }

    @Provides
    GetVersionUsecase provideGetVersionUsecase(OtherRepository remoteRepository) {
        return new GetVersionUsecase(remoteRepository);
    }

    @Provides
    GetDataLuckyMegaUsecase provideGetDataLuckyMegaUsecase(GiftRepository remoteRepository) {
        return new GetDataLuckyMegaUsecase(remoteRepository);
    }
    @Provides
    AddCustomerTotalDialMegaUsecase provideAddCustomerTotalDialMegaUsecase(GiftRepository remoteRepository) {
        return new AddCustomerTotalDialMegaUsecase(remoteRepository);
    }

    @Provides
    AddCustomerGiftMegaUsecase provideAddCustomerGiftMegaUsecase(GiftRepository remoteRepository) {
        return new AddCustomerGiftMegaUsecase(remoteRepository);
    }

    @Provides
    ReadedNotificationSetGiftMegaUsecase  provideReadedNotificationSetGiftMegaUsecase(GiftRepository remoteRepository) {
        return new ReadedNotificationSetGiftMegaUsecase(remoteRepository);
    }

    @Provides
    LogoutUsecase provideLogoutUsecase(AccountRepository remoteRepository) {
        return new LogoutUsecase(remoteRepository);
    }

}

