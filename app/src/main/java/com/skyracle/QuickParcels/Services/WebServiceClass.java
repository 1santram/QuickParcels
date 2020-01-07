package com.skyracle.QuickParcels.Services;

public class WebServiceClass {

    public static String Url="http://quickparcels.in/app/qp/rest_server/";


    public static String METHOD_USERREGISTRATION="user_post";
    public static String METHOD_UPDATEUSER="user_put";
    public static String METHOD_DELETEUSER="user_delete";
    public static String METHOD_GETUSER="user_get_per";
    public static String METHOD_USERLOGIN="user_login";
    public static String METHOD_COURIERCHARGE="get_all_manage_courier_charge";
    public static String METHOD_GETOTP="registration_otp";
    public static String METHOD_GETREGOTP="registration_otp_forUpdate";
    public static String METHOD_UPDATEMOBILE="change_user_mobile_no";
    public static String METHOD_UPDATEPASSWORD="update_password";
    public static String METHOD_ORDERPICKUP="inset_user_pickup_add";
    public static String METHOD_ORDERDROP="inset_user_drop_add";
    public static String METHOD_INSERTADDRESS="inset_user_add_address";
    public static String METHOD_SHOWADDRESS="show_perticular_user_add_address";
    public static String METHOD_DELETEADDRESS="delete_user_add_address";
    public static String METHOD_INSERTORDER="insert_parcel_order";
    public static String METHOD_FORGOTPASS="get_registration_otp_and_userid";
    public static String METHOD_ADDRESSDETAILS="show_details_ofaddress_id";
    public static String METHOD_UPDATEPERADD="update_details_ofaddress_id";
    public static String METHOD_ALlCOUPONS="get_all_coupan";
    public static String METHOD_GETPERCOUPON="get_perticular_coupan";
    public static String METHOD_GETALLORDER="get_order_by_user";
    public static String METHOD_GETSINGLWLORDER="get_order_by_user_order";
    public static String METHOD_CANCELORDER="cancel_order";
    public static String METHOD_APPLYCOUPON="insert_applyied_coupan";
    public static String METHOD_INSERTCOURIERCHARGE="insert_courier_charge_data";
    public static String METHOD_SHOWPARTNER="get_order_by_status_user_order/";


    public static String getMethodShowpartner() {
        return METHOD_SHOWPARTNER;
    }

    public static String getMethodInsertcouriercharge() {
        return METHOD_INSERTCOURIERCHARGE;
    }

    public static String getMethodApplycoupon() {
        return METHOD_APPLYCOUPON;
    }

    public static String getMethodCancelorder() {
        return METHOD_CANCELORDER;
    }

    public static String getMethodGetsinglwlorder() {
        return METHOD_GETSINGLWLORDER;
    }

    public static String getMethodGetallorder() {
        return METHOD_GETALLORDER;
    }

    public static String getMethodGetpercoupon() {
        return METHOD_GETPERCOUPON;
    }

    public static String getMETHOD_ALlCOUPONS() {
        return METHOD_ALlCOUPONS;
    }

    public static String getMethodUpdateperadd() {
        return METHOD_UPDATEPERADD;
    }

    public static String getMethodAddressdetails() {
        return METHOD_ADDRESSDETAILS;
    }

    public static String getMethodForgotpass() {
        return METHOD_FORGOTPASS;
    }

    public static String getMethodInsertorder() {
        return METHOD_INSERTORDER;
    }

    public static String getMethodOrderdrop() {
        return METHOD_ORDERDROP;
    }

    public static String getMethodShowaddress() {
        return METHOD_SHOWADDRESS;
    }

    public static String getMethodDeleteaddress() {
        return METHOD_DELETEADDRESS;
    }

    public static String getMethodOrderpickup() {
        return METHOD_ORDERPICKUP;
    }

    public static String getMethodInsertaddress() {
        return METHOD_INSERTADDRESS;
    }

    public static String getMethodUpdatepassword() {
        return METHOD_UPDATEPASSWORD;
    }

    public static String getMethodUpdatemobile() {
        return METHOD_UPDATEMOBILE;
    }

    public static String getMethodGetregotp() {
        return METHOD_GETREGOTP;
    }

    public static String getMethodCouriercharge() {
        return METHOD_COURIERCHARGE;
    }

    public static String getMethodGetotp() {
        return METHOD_GETOTP;
    }

    public static String getMethodUserlogin() {
        return METHOD_USERLOGIN;
    }

    public static String getMethodUpdateuser() {
        return METHOD_UPDATEUSER;
    }

    public static String getMethodGetuser() {
        return METHOD_GETUSER;
    }

    public static String getUrl() {
        return Url;
    }

    public static String getMethodUserregistration() {
        return METHOD_USERREGISTRATION;
    }

    public static String getMethodGetupdateuser() {
        return METHOD_UPDATEUSER;
    }

    public static String getMethodDeleteuser() {
        return METHOD_DELETEUSER;
    }
}



