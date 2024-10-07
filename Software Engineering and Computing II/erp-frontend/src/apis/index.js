//TEST
const TEST_GET = "/api/test/get";
const TEST_POST = "/api/test/post";

const AUTH = "/api/user/auth"
const LOGIN = "/api/user/login"
const REGISTER = '/api/user/register'

// 商品分类管理
const COMMODITY_CLASSIFICATION_ALL = '/api/category/queryAll'
const COMMODITY_CLASSIFICATION_CREATE = '/api/category/create'
const COMMODITY_CLASSIFICATION_UPDATE = '/api/category/update'
const COMMODITY_CLASSIFICATION_DELETE = '/api/category/delete'

// 商品管理
const COMMODITY_ALL = '/api/product/queryAll';
const COMMODITY_CREATE = '/api/product/create';
const COMMODITY_UPDATE = '/api/product/update';
const COMMODITY_DELETE = '/api/product/delete';


const WAREHOUSE_INPUT = '/api/warehouse/input';
const WAREHOUSE_OUTPUT_PRE = '/api/warehouse/product/count';
const WAREHOUSE_OUTPUT = '/api/warehouse/output';
const WAREHOUSE_GET_INPUTSHEET = '/api/warehouse/inputSheet/state';
const WAREHOUSE_GET_OUTPUTSHEET = '/api/warehouse/outputSheet/state';
const WAREHOUSE_IO_DETAIL_BY_TIME = '/api/warehouse/sheetContent/time';
const WAREHOUSE_IPQ_BY_TIME = '/api/warehouse/inputSheet/time/quantity';
const WAREHOUSE_OPQ_BY_TIME = '/api/warehouse/outputSheet/time/quantity';
const WAREHOUSE_OUTPUTSHEET_APPROVE = '/api/warehouse/outputSheet/approve';
const WAREHOUSE_INPUTSHEET_APPROVE = '/api/warehouse/inputSheet/approve';
const WAREHOUSE_DAILY_COUNT = '/api/warehouse/warehouse/counting';

const WAREHOUSE_GETEXCEL='/api/warehouse/warehouse/excel';//excel


// 销售管理
// 进货管理
const PURCHASE_ALL = '/api/purchase/sheet-show';
const PURCHASE_FIND_SHEET_BY_ID = '/api/purchase/find-sheet';
const PURCHASE_CREATE = '/api/purchase/sheet-make';
const PURCHASE_FIRST_APPROVAL = '/api/purchase/first-approval';
const PURCHASE_SECOND_APPROVAL = '/api/purchase/second-approval';
// 进货退货管理
const PURCHASE_RETURN_ALL = '/api/purchase-returns/sheet-show';
const PURCHASE_RETURN_CREATE = '/api/purchase-returns/sheet-make';
const PURCHASE_RETURN_FIRST_APPROVAL = '/api/purchase-returns/first-approval';
const PURCHASE_RETURN_SECOND_APPROVAL = '/api/purchase-returns/second-approval';
// 销售管理
const SALE_ALL = '/api/sale/sheet-show';
const SALE_CREATE = '/api/sale/sheet-make';
const SALE_FIRST_APPROVAL = '/api/sale/first-approval';
const SALE_SECOND_APPROVAL = '/api/sale/second-approval';
const SALE_FIND_SHEET_BY_ID = '/api/sale/find-sheet';
//销售退货管理
const SALE_RETURN_ALL = '/api/sale-returns/sheet-show';
const SALE_RETURN_CREATE = '/api/sale-returns/sheet-make';
const SALE_RETURN_FIRST_APPROVAL = '/api/sale-returns/first-approval';
const SALE_RETURN_SECOND_APPROVAL = '/api/sale-returns/second-approval';
// 客户管理
const CUSTOMER_CREATE = '/api/customer/create';
const CUSTOMER_DELETE = '/api/customer/delete';
const CUSTOMER_UPDATE = '/api/customer/update';
const CUSTOMER_QUERY_ALL = '/api/customer/query-all';
const CUSTOMER_QUERY_TYPE = '/api/customer/query-by-type';
const CUSTOMER_QUERY_ID = '/api/customer/query-by-id';
// sale_purchase管理
const SALE_PURCHASE_ALL = '/api/purchase/sheet-show';
const SALE_PURCHASE_CREATE = '/api/purchase/sheet-make';
const SALE_CUSTOMER_QUERY = '/api/customer/findByType';
const SALE_CUSTOMER_MAX = '/api/sale/maxAmountCustomer';

// 公司账户管理
const COMPANY_ACCOUNT_CREATE = '/api/company-account/create';
const COMPANY_ACCOUNT_QUERY_ALL = '/api/company-account/queryAll';
const COMPANY_ACCOUNT_UPDATE = '/api/company-account/update';
const COMPANY_ACCOUNT_DELETE = '/api/company-account/delete';
const COMPANY_ACCOUNT_QUERY_NAME = 'api/company-account/queryByName';

//收款管理
const PAY_ALL = '/api/pay/sheet-show';
const PAY_CREATE = '/api/pay/sheet-make';
const PAY_APPROVAL = '/api/pay/approval';

//付款管理
const RECEIVE_ALL = '/api/receive/sheet-show';
const RECEIVE_CREATE = '/api/receive/sheet-make';
const RECEIVE_APPROVAL = '/api/receive/approval';

//打卡管理
const PUNCH_IN='/api/punch-in/punch-in';
const PUNCH_IN_QUERY_ALL='/api/punch-in/query-all';
const PUNCH_IN_QUERY_BY_STAFF_ID='/api/punch-in/query-one';

//销售明细
const SALE_DETAIL='/api/business/sale-detail';
//经营情况
const BUSINESS_SITUATION='/api/business/business-situation';
//经营历程
const BUSINESS_HISTORY = '/api/business/business-history';


//员工管理
const STAFF_CREATE='/api/staff/staff-create';
const STAFF_DELETE='/api/staff/staff-delete';
const STAFF_UPDATE='/api/staff/staff-update';
const STAFF_INFO='/api/staff/query-all-staff';
const POST_INFO='/api/staff/query-all-post';
const POST_DELETE='/api/staff/post-delete';
const POST_UPDATE='/api/staff/post-update';
const POST_CREATE='/api/staff/post-create';

//工资
const SALARY_GENERATE='/api/salary/sheet-make';
const SALARY_SHEET_SHOW='/api/salary/sheet-show';
const SALARY_FIRST_APPROVAL ='/api/salary/first-approval';
const SALARY_SECOND_APPROVAL ='/api/salary/second-approval';
const ANNUAL_GENERATE='/api/salary/annual';

//促销策略
const PROMOTION_STRATEGY_ON_LEVEL_CREATE="/api/sale/PromotionStrategy/OnLevel/create";
const PROMOTION_STRATEGY_ON_LEVEL_GETALL="/api/sale/PromotionStrategy/OnLevel/getall";
const PROMOTION_STRATEGY_ON_LEVEL_UPDATE="/api/sale/PromotionStrategy/OnLevel/update";
const PROMOTION_STRATEGY_ON_LEVEL_DELETE="/api/sale/PromotionStrategy/OnLevel/delete";
const PROMOTION_STRATEGY_ON_AMOUNT_CREATE="/api/sale/PromotionStrategy/OnAmount/create";
const PROMOTION_STRATEGY_ON_AMOUNT_GETALL="/api/sale/PromotionStrategy/OnAmount/getall";
const PROMOTION_STRATEGY_ON_AMOUNT_UPDATE="/api/sale/PromotionStrategy/OnAmount/update";
const PROMOTION_STRATEGY_ON_AMOUNT_DELETE="/api/sale/PromotionStrategy/OnAmount/delete";


module.exports = {
    TEST_GET,
    TEST_POST,

    AUTH,
    LOGIN,
    REGISTER,
    COMMODITY_CLASSIFICATION_ALL,
    COMMODITY_CLASSIFICATION_CREATE,
    COMMODITY_CLASSIFICATION_UPDATE,
    COMMODITY_CLASSIFICATION_DELETE,

    COMMODITY_ALL,
    COMMODITY_CREATE,
    COMMODITY_UPDATE,
    COMMODITY_DELETE,

    WAREHOUSE_INPUT,
    WAREHOUSE_OUTPUT_PRE,
    WAREHOUSE_OUTPUT,
    WAREHOUSE_GET_INPUTSHEET,
    WAREHOUSE_GET_OUTPUTSHEET,
    WAREHOUSE_IO_DETAIL_BY_TIME,
    WAREHOUSE_IPQ_BY_TIME,
    WAREHOUSE_OPQ_BY_TIME,
    WAREHOUSE_OUTPUTSHEET_APPROVE,
    WAREHOUSE_INPUTSHEET_APPROVE,
    WAREHOUSE_DAILY_COUNT,
    WAREHOUSE_GETEXCEL,

    PURCHASE_ALL,
    PURCHASE_CREATE,
    PURCHASE_FIRST_APPROVAL,
    PURCHASE_SECOND_APPROVAL,
    PURCHASE_RETURN_ALL,
    PURCHASE_RETURN_CREATE,
    PURCHASE_RETURN_FIRST_APPROVAL,
    PURCHASE_RETURN_SECOND_APPROVAL,
    PURCHASE_FIND_SHEET_BY_ID,

    SALE_ALL,
    SALE_CREATE,
    SALE_FIRST_APPROVAL,
    SALE_SECOND_APPROVAL,
    SALE_RETURN_ALL,
    SALE_RETURN_CREATE,
    SALE_RETURN_FIRST_APPROVAL,
    SALE_RETURN_SECOND_APPROVAL,
    SALE_CUSTOMER_QUERY,
    SALE_CUSTOMER_MAX,
    SALE_FIND_SHEET_BY_ID,

    CUSTOMER_CREATE,
    CUSTOMER_DELETE,
    CUSTOMER_UPDATE,
    CUSTOMER_QUERY_ALL,
    CUSTOMER_QUERY_TYPE,
    CUSTOMER_QUERY_ID,

    COMPANY_ACCOUNT_CREATE,
    COMPANY_ACCOUNT_QUERY_ALL,
    COMPANY_ACCOUNT_QUERY_NAME,
    COMPANY_ACCOUNT_DELETE,
    COMPANY_ACCOUNT_UPDATE,

    PAY_ALL,
    PAY_CREATE,
    PAY_APPROVAL,

    RECEIVE_CREATE,
    RECEIVE_ALL,
    RECEIVE_APPROVAL,

    PUNCH_IN,
    PUNCH_IN_QUERY_ALL,
    PUNCH_IN_QUERY_BY_STAFF_ID,

    SALE_DETAIL,
    BUSINESS_SITUATION,
    BUSINESS_HISTORY,

    STAFF_CREATE,
    STAFF_INFO,
    STAFF_DELETE,
    STAFF_UPDATE,
    POST_INFO,
    POST_UPDATE,
    POST_DELETE,
    POST_CREATE,

    SALARY_GENERATE,
    SALARY_SHEET_SHOW,
    SALARY_FIRST_APPROVAL,
    SALARY_SECOND_APPROVAL,
    ANNUAL_GENERATE,

    PROMOTION_STRATEGY_ON_LEVEL_CREATE,
    PROMOTION_STRATEGY_ON_LEVEL_GETALL,
    PROMOTION_STRATEGY_ON_LEVEL_UPDATE,
    PROMOTION_STRATEGY_ON_LEVEL_DELETE,

    PROMOTION_STRATEGY_ON_AMOUNT_CREATE,
    PROMOTION_STRATEGY_ON_AMOUNT_GETALL,
    PROMOTION_STRATEGY_ON_AMOUNT_UPDATE,
    PROMOTION_STRATEGY_ON_AMOUNT_DELETE
};
