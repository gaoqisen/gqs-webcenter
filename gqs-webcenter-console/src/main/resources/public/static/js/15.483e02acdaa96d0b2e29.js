webpackJsonp([15],{de6X:function(e,t,n){"use strict";var i={render:function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("aside",{staticClass:"site-sidebar",class:"site-sidebar--"+e.sidebarLayoutSkin},[n("div",{staticClass:"site-sidebar__inner"},[n("el-menu",{staticClass:"site-sidebar__menu",attrs:{"default-active":e.menuActiveName||"home",collapse:e.sidebarFold,collapseTransition:!1}},[n("el-menu-item",{attrs:{index:"home"},on:{click:function(t){return e.$router.push({name:"home"})}}},[n("i",{staticClass:"el-icon-s-home"}),e._v(" "),n("span",{attrs:{slot:"title"},slot:"title"},[e._v("首页")])]),e._v(" "),e._l(e.menuList,function(t){return n("sub-menu",{key:t.menuId,attrs:{menu:t,dynamicMenuRoutes:e.dynamicMenuRoutes}})})],2)],1)])},staticRenderFns:[]};t.a=i},l2bK:function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i,s=n("HALf"),a=(i=s)&&i.__esModule?i:{default:i},o=n("E4LH");t.default={data:function(){return{dynamicMenuRoutes:[]}},components:{SubMenu:a.default},computed:{sidebarLayoutSkin:{get:function(){return this.$store.state.common.sidebarLayoutSkin}},sidebarFold:{get:function(){return this.$store.state.common.sidebarFold}},menuList:{get:function(){return this.$store.state.common.menuList},set:function(e){this.$store.commit("common/updateMenuList",e)}},menuActiveName:{get:function(){return this.$store.state.common.menuActiveName},set:function(e){this.$store.commit("common/updateMenuActiveName",e)}},mainTabs:{get:function(){return this.$store.state.common.mainTabs},set:function(e){this.$store.commit("common/updateMainTabs",e)}},mainTabsActiveName:{get:function(){return this.$store.state.common.mainTabsActiveName},set:function(e){this.$store.commit("common/updateMainTabsActiveName",e)}}},watch:{$route:"routeHandle"},created:function(){this.menuList=JSON.parse(sessionStorage.getItem("menuList")||"[]"),this.dynamicMenuRoutes=JSON.parse(sessionStorage.getItem("dynamicMenuRoutes")||"[]"),this.routeHandle(this.$route)},methods:{routeHandle:function(e){if(e.meta.isTab){var t=this.mainTabs.filter(function(t){return t.name===e.name})[0];if(!t){if(e.meta.isDynamic&&!(e=this.dynamicMenuRoutes.filter(function(t){return t.name===e.name})[0]))return console.error("未能找到可用标签页!");t={menuId:e.meta.menuId||e.name,name:e.name,title:e.meta.title,type:(0,o.isURL)(e.meta.iframeUrl)?"iframe":"module",iframeUrl:e.meta.iframeUrl||"",params:e.params,query:e.query},this.mainTabs=this.mainTabs.concat(t)}this.menuActiveName=t.menuId+"",this.mainTabsActiveName=t.name}}}}},"z/ys":function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var i=n("l2bK"),s=n.n(i);for(var a in i)"default"!==a&&function(e){n.d(t,e,function(){return i[e]})}(a);var o=n("de6X"),u=n("C7Lr")(s.a,o.a,!1,null,null,null);t.default=u.exports}});
//# sourceMappingURL=15.483e02acdaa96d0b2e29.js.map