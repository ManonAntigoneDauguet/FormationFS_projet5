import accountSpec from './account.cy';
import loginSpec from './login.cy';
import registerSpec from './register.cy';
import sessionAdminSpec from './sessions-admin.cy';
import sessionNotAdminSpec from './sessions-not-admin.cy';

/// <reference types="cypress" />
accountSpec();
loginSpec();
registerSpec();
sessionAdminSpec();
sessionNotAdminSpec();