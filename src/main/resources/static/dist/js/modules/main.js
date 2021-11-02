define(function(require, exports, module) {
    var plugins = require('plugins');
	var Authc = require('authc');

    var wpexLocalize = {
    		"mobileMenuOpen" : "Click here to navigate",
    		"mobileMenuClosed" : "Close navigation",
    		"isOriginLeft" : "1"
    	};
    
    // 图片懒加载
    // var imagesLazyload = function () {
    // 	require.async('lazyload', function () {
    // 		$("img").lazyload({
	//    	   		 placeholder: _MTONS.BASE_PATH + '/dist/images/spinner.gif',
	//    	   		 effect: "fadeIn"
	//    	   	});
    //     });
    // }
    
    // 返回顶部
    var backToTop = function () {
    	var $window = $(window);
    	var $scrollTopLink = $( 'a.site-scroll-top' );
		$window.scroll(function () {
			if ($(this).scrollTop() > 100) {
				$scrollTopLink.fadeIn();
			} else {
				$scrollTopLink.fadeOut();
			}
		});		
		$scrollTopLink.on('click', function() {
			$( 'html, body' ).animate({scrollTop:0}, 400);
			return false;
		} );
    }
    
	// 绑定按钮事件
	var bindClickEvent = function () {
		// Favor
		$('a[rel=favor]').click(function () {
			var id = $(this).attr('data-id');

			if (!Authc.isAuthced()) {
				Authc.showLogin();
				return false;
			}

			if (parseInt(id) > 0) {
				jQuery.getJSON(_MTONS.BASE_PATH +'/user/favor', {'id': id}, function (ret) {
					if (ret.code >=0) {
						var favors = $('#favors').text();
						$('#favors').text(parseInt(favors) + 1);
						layer.msg("您已收藏成功!", {icon: 6});
					} else {
						layer.msg(ret.message, {icon: 5});
					}
				});
			}
		});
		//$(document).pjax('a[rel=pjax]', '#wrap', {
		//	fragment: '#wrap',
		//	timeout: 10000,
		//	maxCacheLength: 0
		//});
	};


	var bindClickEvent2 = function () {
		// UnFavor
		$('a[rel=unfavor]').click(function () {
			var id = $(this).attr('data-id');
			if (!Authc.isAuthced()) {
				Authc.showLogin();
				return false;
			}

			if (parseInt(id) > 0) {
				jQuery.getJSON(_MTONS.BASE_PATH +'/user/unfavor', {'id': id}, function (ret) {
					if (ret.code >=0) {
						var favors = $('#favors').text();
						$('#favors').text(parseInt(favors) - 1);
						layer.msg("取消收藏成功!", {icon: 2});
					} else {
						layer.msg(ret.message, {icon: 5});
					}
				});
			}
		});
	};



    exports.init = function () {
    	// imagesLazyload();
    	backToTop();
		bindClickEvent();
		bindClickEvent2();
        $('[data-toggle="tooltip"]').tooltip();
    };
    
});