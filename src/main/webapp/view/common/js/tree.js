//树查询
function searchDept(searchText){
	var id = this.getAttribute("id");
	//var searchText = $("#searchText").val();
	var treeId = this.getAttribute("treeId");
	$("#"+treeId).tree("search", searchText);
}


(function($) { 

    $.extend($.fn.tree.methods, {
        /**
         * 扩展easyui tree的搜索方法
         * @param tree easyui tree的根DOM节点(UL节点)的jQuery对象
         * @param searchText 检索的文本
         * @param this-context easyui tree的tree对象
         */
        search: function(jqTree, searchText) {
            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
            var tree = this;

            //获取所有的树节点
            var nodeList = getAllNodes(jqTree, tree);

            //如果没有搜索条件，则展示所有树节点
            searchText = $.trim(searchText);
            if (searchText == "") {
                for (var i=0; i<nodeList.length; i++) {
                    $(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
                    $(nodeList[i].target).show();
                    
                    //展开所有子节点
                    showChildrenNode(jqTree, tree, nodeList[i]);
                }
                //展开已选择的节点（如果之前选择了）
                var selectedNode = tree.getSelected(jqTree);
                if (selectedNode) {
                    tree.expandTo(jqTree, selectedNode.target);
                }
                
                return;
            }

            //搜索匹配的节点并高亮显示
            var matchedNodeList = [];
            if (nodeList && nodeList.length>0) {
                var node = null;
                for (var i=0; i<nodeList.length; i++) {
                    node = nodeList[i];
                    if (isMatch(searchText, node.text)) {
                        matchedNodeList.push(node);
                    }
                }

                //隐藏所有节点
                for (var i=0; i<nodeList.length; i++) {
                    $(".tree-node-targeted", nodeList[i].target).removeClass("tree-node-targeted");
                    $(nodeList[i].target).hide();
                }           

                //折叠所有节点
                tree.collapseAll(jqTree);

                //展示所有匹配的节点以及父节点            
                for (var i=0; i<matchedNodeList.length; i++) {
                    showMatchedNode(jqTree, tree, matchedNodeList[i]);
                    
                    showChildrenNode(jqTree, tree, matchedNodeList[i]);
                }
            }    
        },

        /**
         * 展示节点的子节点（子节点有可能在搜索的过程中被隐藏了）
         * @param node easyui tree节点
         */
        showChildren: function(jqTree, node) {
            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
            var tree = this;

            //展示子节点
            if (!tree.isLeaf(jqTree, node.target)) {
                var children = tree.getChildren(jqTree, node.target);
                if (children && children.length>0) {
                    for (var i=0; i<children.length; i++) {
                        if ($(children[i].target).is(":hidden")) {
                            $(children[i].target).show();
                        }
                    }
                }
            }   
        },

        /**
         * 将滚动条滚动到指定的节点位置，使该节点可见（如果有滚动条才滚动，没有滚动条就不滚动）
         * @param param {
         *    treeContainer: easyui tree的容器（即存在滚动条的树容器）。如果为null，则取easyui tree的根UL节点的父节点。
         *    targetNode:  将要滚动到的easyui tree节点。如果targetNode为空，则默认滚动到当前已选中的节点，如果没有选中的节点，则不滚动
         * } 
         */
        scrollTo: function(jqTree, param) {
            //easyui tree的tree对象。可以通过tree.methodName(jqTree)方式调用easyui tree的方法
            var tree = this;

            //如果node为空，则获取当前选中的node
            var targetNode = param && param.targetNode ? param.targetNode : tree.getSelected(jqTree);

            if (targetNode != null) {
                //判断节点是否在可视区域               
                var root = tree.getRoot(jqTree);
                var $targetNode = $(targetNode.target);
                var container = param && param.treeContainer ? param.treeContainer : jqTree.parent();
                var containerH = container.height();
                var nodeOffsetHeight = $targetNode.offset().top - container.offset().top;
                if (nodeOffsetHeight > (containerH - 30)) {
                    var scrollHeight = container.scrollTop() + nodeOffsetHeight - containerH + 30;
                    container.scrollTop(scrollHeight);
                }                           
            }
        }
    });
    

    /**
     * 展示搜索匹配的节点
     */
    function showMatchedNode(jqTree, tree, node) {
        //展示所有父节点
        $(node.target).show();
        $(".tree-title", node.target).addClass("tree-node-targeted");
        var pNode = node;
        while ((pNode = tree.getParent(jqTree, pNode.target))) {
            $(pNode.target).show();            
        }
        //展开到该节点
        tree.expandTo(jqTree, node.target);
        //如果是非叶子节点，需折叠该节点的所有子节点
        if (!tree.isLeaf(jqTree, node.target)) {
            tree.collapse(jqTree, node.target);
        }
    } 
    
    /**
     * 展示所有子节点
     */
    function showChildrenNode(jqTree, tree, node){
    	
    	$(node.target).show();
        $(".tree-title", node.target).addClass("tree-node-targeted");
        tree.expandTo(jqTree, node.target);
    	//展示其所有子节点
        var children = tree.getChildren(jqTree, node.target);
        for (var i=0; i<children.length; i++) {
        	showChildrenNode(jqTree, tree, children[i]);
        }
    }

    /**
     * 判断searchText是否与targetText匹配
     * @param searchText 检索的文本
     * @param targetText 目标文本
     * @return true-检索的文本与目标文本匹配；否则为false.
     */
    function isMatch(searchText, targetText) {
        return $.trim(targetText)!="" && targetText.indexOf(searchText)!=-1;
    }

    /**
     * 获取easyui tree的所有node节点
     */
    function getAllNodes(jqTree, tree) {
        var allNodeList = jqTree.data("allNodeList");
        if (!allNodeList) {
            var roots = tree.getRoots(jqTree);
            allNodeList = getChildNodeList(jqTree, tree, roots);
            jqTree.data("allNodeList", allNodeList);
        }
        return allNodeList;
    }

    /**
     * 定义获取easyui tree的子节点的递归算法
     */
    function getChildNodeList(jqTree, tree, nodes) {
        var childNodeList = [];
        if (nodes && nodes.length>0) {              
            var node = null;
            for (var i=0; i<nodes.length; i++) {
                node = nodes[i];
                childNodeList.push(node);
                if (!tree.isLeaf(jqTree, node.target)) {
                    var children = tree.getChildren(jqTree, node.target);
                    childNodeList = childNodeList.concat(getChildNodeList(jqTree, tree, children));
                }
            }
        }
        return childNodeList;
    }
})(jQuery);

/*
 * 
 * LoadData：装载数据
 * PackageData 打包数据
 * UnpackageData 恢复数据
 * SaveOption 保存条件
 * RestoreOption 恢复条件
 * GetIds
 * */
function loadDataTree(tree,url,param, setOption, ifyes,ifno, sync) {
	console.log("loadDataTree开始..");
    if ($("#"+tree).attr("dataCheckBox") == "Y") {
        var checkbox = true;
    } else {
        var checkbox = false;
    }
    $("#"+tree).tree({
		checkbox : checkbox,
		multiple : true,
		data:null,
		loadFilter : function(data) {
			if(data.rows)
				return data.rows;
			else 
				return data;
		},
		onClick : function(node) {
			setStorage('selectTree', node);
			setPanelId($("#"+tree), node.id);
			id = node.id;
			eval($("#"+tree).attr("trigger"));
		},
		onSelect : function(node) {
			id = node.id;
		}
	});
	ajaxQueryTree($("#"+tree), url, param, setOption, ifyes, sync);
}
// getids用于获取多个选中时候的id
function getids(){
	//TODO
}
function setids(){
	//TODO
}
//保存树状态 back时和弹出层关闭刷新是调用
function getOptionTree(tree){
	console.log("getOptionTree开始..");
	var path=getPath($("#"+tree));
	if ($("#"+tree).tree('getSelected')) {
		setStorage(path+"_backStorage_option", $("#"+tree).tree('getSelected'));
		setStorage(path+"_backStorage_data", $("#"+tree).tree('getRoots'));
	} else {
		setStorage(path+"_backStorage_option" , getStorage('selectTree'));
	}
}
//恢复树状态
// 保存树数据
function packageTree() {
	console.log("packageTree开始..");
	$('.easyui-tree').each(
			function() {
				var path=getPath($(this));
				setStorage(path+"_backStorage_option", $(this).tree('getSelected'));
				setStorage(path+"_backStorage_data", $(this).tree('getRoots'));
			});

}

function unpackageTree(tree) {
	console.log("unpackageTree开始..");
	var path=getPath($("#"+tree));
	var storageRoot = getStorage(path+"_backStorage_data");
	if (storageRoot) {
		$('#'+tree).tree({
			data:storageRoot
		});
	}
}
// 私有方法，合并两颗树的属性 但是与extend js方法不同  恢复树状态
function setOptionTree(tree) {
	console.log("setOptionTree开始..");
	var path=getPath(tree);
	
	var storageData = getStorage(path+"_backStorage_data");
	var storageOption = getStorage(path+"_backStorage_option");
	if (storageData && storageOption) {
		// 设置展开状态以及选中状态 选中节点需要特殊处理
//		if (storageOption && storageOption.id == tree.tree('getRoots')[0].id) {
//			tree.tree('select', tree.tree('getRoots')[0].target);
//		} else {
			extend(tree, storageData, storageOption);
//		}
	}
}

//私有合并两个树的代码
function extend(tree, storageData, storageOption){
	var des = tree.tree('getChildren', tree.tree('getRoots')); // alert(childs.length);
	var flg = -1;
	var arr = new Array();
	for (var i = 0; i < des.length; i++) {
		for (var j = 0; j < storageData.length; j++) {
			if (des[i].id == storageData[j].id) {
				if (storageData[j].state == 'closed') {
					tree.tree('collapse', des[i].target);
				}
			}
		}
		if (storageOption&&storageOption.id == des[i].id) {
			flg = storageOption.id;
			tree.tree('select', des[i].target);
		}
		if(storageOption){
			arr[i] = Math.abs(des[i].id - storageOption.id);
		}
	}
	// 没有匹配到节点 选择最近节点
	if (flg == -1&&storageOption) {
		var max = arr[0];
		var min = arr[0];
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] < min) {
				min = arr[i];
			}
		}
		
		var k = 0;
		for (var i = 0; i < arr.length; i++) {
			if (min == arr[i]) {
				k += i;
			}
		}
		if(des.length==k){
			console.log("最接近的树节点下标="+k);
			console.log("最接近的树节点id="+des[k-1].id);
			tree.tree('select', des[k-1].target);
			setPanelId(tree.attr('id'), des[k-1].id);
			setStorage('selectTree',tree.tree('find', des[k-1].id));
		} else {
			for (var i = 0; i < des.length; i++) {
				if(i==k){
					console.log("最接近的树节点下标="+k);
					console.log("最接近的树节点id="+des[i].id);
					tree.tree('select', des[i].target);
					setPanelId(tree.attr('id'), des[i].id);
					setStorage('selectTree',tree.tree('find', des[i].id));
					break;
				}
			}
		}
	}

}
function loadTransformTree(out, res){
	$("#"+out).tree('loadData', res.data.rows);
}