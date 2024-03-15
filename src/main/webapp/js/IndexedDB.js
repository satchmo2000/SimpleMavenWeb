(function () {
    /**
     * It's important to note that IndexedDB doesn't work for content loaded into a frame 
     * from another site (either <frame> or <iframe>. This is a security measure.
     */

    if (typeof indexedDB == "undefined") {
        if (typeof mozIndexedDB != "undefined")
            indexedDB = mozIndexedDB;
        else if (typeof webkitIndexedDB != "undefined")
            indexedDB = webkitIndexedDB;
    }

    window['IndexedDB'] = {};                                              //创建IndexedDB命名空间
    IndexedDB.Config = {
        name: 'mybim',
        version: 1.0,
        tbname: 'tb_bim',           //按构件存储数据
        tbbatch: 'tb_batch',        //按组存储数据
        tbparam: 'tb_param',        //存储各种参数
        desc: 'manage my bim data'
    };

    window['IDBTransaction'] = {};
    IDBTransaction.READ_ONLY = "readonly";
    IDBTransaction.READ_WRITE = "readwrite";
    IDBTransaction.VERSION_CHANGE = "versionchange";


    function errorCallback(error) {
        //console.error('error:' + error.message);
    }

    function CheckValid() {
        if (typeof indexedDB == "undefined") {
            return false;
        }
        else
            return true;
    }
    window['IndexedDB']['CheckValid'] = CheckValid;

    var request;
    var db;
    var objectStore;
    var paramStore;

    //1、创建物理表（有则创建，创建并打开成功后执行模型加载操作）
    function CreateTable(startFunc , loadedFunc) {

        request = indexedDB.open(IndexedDB.Config.name, 1);
        request.onsuccess = function (e) {
            db = e.target.result;
            
            if (db.version != "1.0") {
                var requestVersion = db.setVersion("1.0");
                requestVersion.onerror = function (event) {
                    console.log(event);
                }
                requestVersion.onsuccess = function (event) {
                    db = event.target.result;
                    var transaction = db.transaction(IndexedDB.Config.tbbatch, IDBTransaction.READ_WRITE);
                    objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
                    var transaction2 = db.transaction(IndexedDB.Config.tbparam, IDBTransaction.READ_WRITE);
                    paramStore = transaction2.objectStore(IndexedDB.Config.tbparam);
                    console.log(event);
                }
            }
            else {
                var transaction = db.transaction(IndexedDB.Config.tbbatch, IDBTransaction.READ_WRITE);
                objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
                var transaction2 = db.transaction(IndexedDB.Config.tbparam, IDBTransaction.READ_WRITE);
                paramStore = transaction2.objectStore(IndexedDB.Config.tbparam);
            }
            if (startFunc)
                startFunc(loadedFunc);
        }
        request.onupgradeneeded = function (e) {
            db = e.target.result;
            createObjectStore();
        };

        request.onerror = function (e) {
            alert(e.target.errorCode);
            console.log("Database error: " + e.target.errorCode);
        };
    }
    window['IndexedDB']['CreateTable'] = CreateTable;

    //创建存储对象tb_bim（按构件存储），tb_batch（按块或批次存储）
    function createObjectStore() {
        if (!db.objectStoreNames.contains(IndexedDB.Config.tbname)) {
            var objectStore1 = db.createObjectStore(IndexedDB.Config.tbname, {
                keyPath: "id"
            });

            objectStore1.createIndex('TaskId', 'tkid', { unique: false });
            objectStore1.createIndex('BatchId', 'batchid', { unique: false });
        }
        if (!db.objectStoreNames.contains(IndexedDB.Config.tbbatch)) {
            var objectStore2 = db.createObjectStore(IndexedDB.Config.tbbatch, {
                keyPath: "id"
            });

            objectStore2.createIndex('IndexId', 'id', { unique: false });
        }
        if (!db.objectStoreNames.contains(IndexedDB.Config.tbparam)) {
            var objectStore3 = db.createObjectStore(IndexedDB.Config.tbparam, {
                keyPath: "id"
            });

            objectStore3.createIndex('IndexId', 'id', { unique: false });
        }
    }

    function deleteDB() {
        indexedDB.deleteDatabase(IndexedDB.Config.name);
        console.log(IndexedDB.Config.name + '数据库已删除');
        alert(IndexedDB.Config.name + '数据库已删除');
    }
    window['IndexedDB']['deleteDB'] = deleteDB;

    function closeDB() {
        if (db) {
            db.close();
            console.log(IndexedDB.Config.name + '数据库被关闭');
            alert(IndexedDB.Config.name + '数据库被关闭');
        }
        else {
            console.log(IndexedDB.Config.name + '数据库不存在或未打开');
        }
    }
    window['IndexedDB']['closeDB'] = closeDB;

    //=========================模式1：按构件操作====================================

    //2、添加记录数据(存储的batchid=tkid*1000 + batchid)
    function addData(id,tkid,batchid,data,levelid,statusid) {
        var transaction = db.transaction([IndexedDB.Config.tbname], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbname);
        var requestAdd = objectStore.add({
            id: id,
            tkid: tkid,
            batchid: tkid * 1000 + batchid,
            data: data,
            levelid: levelid,
            statusid: statusid
        });

        requestAdd.onerror = function (event) {
            console.log('invalid data');
        };

        requestAdd.onsuccess = function (event) {
            console.log('add data success , id=' + id + ',tkid=' + tkid + ',batchid=' + batchid + ',levelid=' + levelid + ',statusid=' + statusid);
        };
    }
    window['IndexedDB']['addData'] = addData;

    //3、修改数据状态
    function updateData(id, levelid, statusid) {
        var transaction = db.transaction([IndexedDB.Config.tbname], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbname);
        var requestData = objectStore.get(id);

        requestData.onerror = function (event) {
            console.log(event);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                var row = event.target.result;
                row.levelid = levelid;
                row.statusid = statusid;
                objectStore.put(row);
            }
        }
    }
    window['IndexedDB']['updateData'] = updateData;

    //4、删除一批数据（条件：tkid=? and batchid=?）
    function deleteData(tkid, batchid) {
        var transaction = db.transaction([IndexedDB.Config.tbname], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbname);
        //根据搜索条件tkid=? and batchid=?删除相应的记录集
        var id = 0;
        objectStore.delete(id);
    }
    window['IndexedDB']['deleteData'] = deleteData;

    //5、清除记录数据
    function clearData() {
        var transaction = db.transaction([IndexedDB.Config.tbname], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbname);
        objectStore.clear();
    }
    window['IndexedDB']['clearData'] = clearData;

    //6、根据条件获取一批数据（条件：searchid = tkid * 1000 + batchid=?）
    function getData(tkid, batchid, successFunc, errorFunc) {
        var searchid = tkid * 1000 + batchid;

        var transaction = db.transaction([IndexedDB.Config.tbname], IDBTransaction.READ_ONLY);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbname);
        var index = objectStore.index('BatchId');
        var requestData = index.index(searchid);

        requestData.onerror = function (event) {
            if (errorFunc)
                errorFunc(searchid);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                if (successFunc) {
                    successFunc(event.target.result.data, searchid);
                }
            }
            else {
                if (errorFunc)
                    errorFunc(searchid);
            }
        }
    }
    window['IndexedDB']['getData'] = getData;

    //=========================模式2：按块操作====================================

    //2、添加批数据（id=tkid*10000+batchid）
    function addBatchData(id,data) {
        var transaction = db.transaction([IndexedDB.Config.tbbatch], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
        var requestAdd = objectStore.add({
            id: id,
            data: data,
            count: 0
        });

        requestAdd.onerror = function (event) {
            console.log('invalid data');
        };

        requestAdd.onsuccess = function (event) {
            console.log('add batch data success , id=' + id);
        };
    }
    window['IndexedDB']['addBatchData'] = addBatchData;

    //3、更新批数据的修改记录数（id=tkid*10000+batchid）
    function updateBatchData(id) {
        var transaction = db.transaction([IndexedDB.Config.tbbatch], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
        var requestData = objectStore.get(id);

        requestData.onerror = function (event) {
            console.log(event);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                var row = event.target.result;
                row.count = row.count + 1;
                console.log('update batch data count = ' + row.count + ' , id=' + id);
                objectStore.put(row);
            }
        }
    }
    window['IndexedDB']['updateBatchData'] = updateBatchData;

    //4、删除一批数据（id=tkid*10000+batchid）
    function deleteBatchData(id) {
        var transaction = db.transaction([IndexedDB.Config.tbbatch], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
        objectStore.delete(id);
        console.log('delete batch data success , id=' + id);
    }
    window['IndexedDB']['deleteBatchData'] = deleteBatchData;

    //5、清除批数据
    function clearBatchData() {
        var transaction = db.transaction([IndexedDB.Config.tbbatch], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);
        objectStore.clear();
    }
    window['IndexedDB']['clearBatchData'] = clearBatchData;

    //6、删除变更的数据
    function removeChangeData() {
        var transaction = db.transaction(IndexedDB.Config.tbbatch, IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);

        var requestData = objectStore.getAll();

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                var ds = event.target.result;

                for (var index = 0; index < ds.length; index++) {
                    if (ds[index].count > 0) {
                        console.log('remove batch data success , id=' + ds[index].id);
                        objectStore.delete(ds[index].id);
                    }
                }
            }
        }
    }
    window['IndexedDB']['removeChangeData'] = removeChangeData;

    //根据条件获取一条数据（条件：id=tkid*10000+batchid）
    function getBatchData(id , successFunc, errorFunc) {
        var transaction = db.transaction(IndexedDB.Config.tbbatch, IDBTransaction.READ_ONLY);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbbatch);

        var requestData = objectStore.get(id);

        requestData.onerror = function (event) {
            if(errorFunc)
                errorFunc(id);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                if (successFunc) {
                    successFunc(event.target.result.data, id);
                }
                console.log('load batch data success , id=' + id);
            }
            else {
                if (errorFunc)
                    errorFunc(id);
            }
        }
    }
    window['IndexedDB']['getBatchData'] = getBatchData;

    //=========================模式3：参数操作====================================
    function addParam(id, value) {
        var transaction = db.transaction([IndexedDB.Config.tbparam], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbparam);
        var requestAdd = objectStore.add({
            id: id,
            data: value
        });

        requestAdd.onerror = function (event) {
            console.log('invalid data or exist the same id');
            updateParam(id, value);
        };

        requestAdd.onsuccess = function (event) {
            console.log('add param data success , id=' + id);
        };
    }
    window['IndexedDB']['addParam'] = addParam;

    function updateParam(id, value) {
        var transaction = db.transaction([IndexedDB.Config.tbparam], IDBTransaction.READ_WRITE);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbparam);
        var requestData = objectStore.get(id);

        requestData.onerror = function (event) {
            addParam(id, value);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                var row = event.target.result;
                row.data = value;
                objectStore.put(row);
            }
        }
    }
    window['IndexedDB']['updateParam'] = updateParam;

    function getParam(id, successFunc, errorFunc) {
        var transaction = db.transaction(IndexedDB.Config.tbparam, IDBTransaction.READ_ONLY);
        var objectStore = transaction.objectStore(IndexedDB.Config.tbparam);

        var requestData = objectStore.get(id);

        requestData.onerror = function (event) {
            if (errorFunc)
                errorFunc(id);
        };

        requestData.onsuccess = function (event) {
            if (event.target.result) {
                if (successFunc) {
                    successFunc(event.target.result.data, id);
                }
                console.log('load param data success , id=' + id);
            }
            else {
                if (errorFunc)
                    errorFunc(id);
            }
        }
    }
    window['IndexedDB']['getParam'] = getParam;
})();