(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-b24d8342"],{"51f4":function(e,l,t){},c7f6:function(e,l,t){"use strict";t.r(l);var a=function(){var e=this,l=e.$createElement,t=e._self._c||l;return t("a-modal",{attrs:{destroyOnClose:!0,width:1080,visible:e.visible,title:e.getTitle(e.initvalue)},model:{value:e.visible,callback:function(l){e.visible=l},expression:"visible"}},[t("div",{staticClass:"main"},[t("a-form-model",{ref:"form",attrs:{layout:e.formLayout,model:e.form,rules:e.rules}},[t("a-form-model-item",{directives:[{name:"show",rawName:"v-show",value:!1,expression:"false"}],attrs:{label:"id",prop:"id","label-col":e.formItemLayout.labelCol,"wrapper-col":e.formItemLayout.wrapperCol}},[t("a-input",{model:{value:e.form.id,callback:function(l){e.$set(e.form,"id",l)},expression:"form.id"}})],1),t("a-form-model-item",{attrs:{label:"名称",prop:"name","label-col":e.formItemLayout.labelCol,"wrapper-col":e.formItemLayout.wrapperCol}},[t("a-input",{attrs:{placeholder:"请输入名称"},model:{value:e.form.name,callback:function(l){e.$set(e.form,"name",l)},expression:"form.name"}})],1)],1)],1),t("template",{slot:"footer"},[t("a-button",{attrs:{type:"primary"},on:{click:function(l){return e.handleEdit()}}},[e._v(" 提交 ")]),t("a-button",{on:{click:function(l){return e.handleClose()}}},[e._v(" 取消 ")])],1)],2)},n=[],o=t("2410"),i=t.n(o),r=t("b775"),u=t("f64c"),s={labelCol:{span:3},wrapperCol:{span:21}},m={components:{},props:{initvalue:{type:Object,default:null}},watch:{initvalue:function(){if(this.initvalue)for(var e in this.initvalue)this.form[e]=this.initvalue[e];else this.form=i()(this.initForm)},visible:function(e,l){if(!e)try{this.form=i()(this.initForm)}catch(t){}}},methods:{getTitle:function(e){var l=this.$createElement;return l("span",e?[" 编辑 "]:[" 新建 "])},show:function(){this.visible=!0},handleClose:function(){this.form=i()(this.initForm),this.form.param="",this.visible=!1},handleEdit:function(){var e=this;this.$refs.form.validate((function(l){if(l){var t=JSON.parse(JSON.stringify(e.form));e.commonRequest.head.operationTime=Date.now(),e.commonRequest.body=t;var a=e.commonRequest;Object(r["b"])({url:"/archetypesExecute/addOrEdit",method:"post",dataType:"json",data:a}).then((function(l){"S"===l.head.status?(u["a"].success(l.head.msg),e.handleClose(),e.$emit("refresh")):u["a"].error(l.head.msg)})).catch((function(e){console.log(e)}))}}))}},data:function(){return{visible:!1,formLayout:"horizontal",formItemLayout:s,datasourcesList:[],form:{id:null,archetypesId:null,auditId:null,applicationCode:null,name:null,url:null,basePackage:null,groupId:null,artifactId:null,version:null,description:null,remark:null,gmtCreated:null,creator:null,creatorTenantId:null,creatorCode:null,gmtModified:null,modifier:null,modifierCode:null,auditStatus:null,status:null,bak01:null,bak02:null,bak03:null,bak04:null,bak05:null,end:""},initForm:{id:null,archetypesId:null,auditId:null,applicationCode:null,name:null,url:null,basePackage:null,groupId:null,artifactId:null,version:null,description:null,remark:null,gmtCreated:null,creator:null,creatorTenantId:null,creatorCode:null,gmtModified:null,modifier:null,modifierCode:null,auditStatus:null,status:null,bak01:null,bak02:null,bak03:null,bak04:null,bak05:null,end:""},rules:{name:[{required:!0,message:"请输入"},{max:50,message:"最多输入50个字符"},{pattern:/^(?!(\s+$))/,message:"输入格式错误"}]},commonRequest:{head:{operationTime:Date.now(),appId:"bk_18aef9dae4544e65aa9132abe252b649"},body:{}},bodyById:{id:0}}}},d=m,c=(t("e7ca"),t("2877")),f=Object(c["a"])(d,a,n,!1,null,null,null);l["default"]=f.exports},e7ca:function(e,l,t){"use strict";t("51f4")}}]);