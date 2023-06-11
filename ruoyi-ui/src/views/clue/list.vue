<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryRef" :model="queryParams" :inline="true" label-width="68px">
            <el-form-item label="线索名称" prop="name">
                <el-input
                    v-model="queryParams.name"
                    placeholder="请输入线索名称"
                    clearable
                    style="width: 240px"
                    @keyup.enter="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="线索状态" clearable style="width: 240px">
                    <el-option
                        v-for="dict in sys_clue_status"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间" style="width: 308px">
                <el-date-picker
                    v-model="dateRange"
                    value-format="YYYY-MM-DD"
                    type="daterange"
                    range-separator="-"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                ></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
                <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['sys:clue:add']"
                    type="primary"
                    plain
                    icon="Plus"
                    @click="handleAdd"
                    >新增</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['sys:clue:edit']"
                    type="success"
                    plain
                    icon="Edit"
                    :disabled="single"
                    @click="handleUpdate"
                    >修改</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['sys:clue:remove']"
                    type="danger"
                    plain
                    icon="Delete"
                    :disabled="multiple"
                    @click="handleDelete"
                    >删除</el-button
                >
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['sys:clue:export']"
                    type="warning"
                    plain
                    icon="Download"
                    @click="handleExport"
                    >导出</el-button
                >
            </el-col>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 表格数据 -->
        <el-table v-loading="loading" :data="clueList" @selectionChange="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="线索编号" prop="clueSn" width="120" />
            <el-table-column label="线索名称" prop="name" :show-overflow-tooltip="true"/>
            <el-table-column label="分配用户" prop="userName" :show-overflow-tooltip="true"/>
            <el-table-column label="分配部门" prop="deptName" :show-overflow-tooltip="true"/>
            <el-table-column label="手机号码" prop="mobile" :show-overflow-tooltip="true"/>
            <el-table-column label="归属地" prop="mobileCity"/>
            <el-table-column label="IP地址" prop="ipaddr" width="100"/>
            <el-table-column label="IP城市" prop="ipCity"/>
            <el-table-column label="资源状态" align="center" width="100">
              <template #default="scope">
                <el-tag type="success" v-if="scope.row.userId">已分配</el-tag>
                <el-tag type="warning" v-else>未分配</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="使用状态" align="center" width="100">
              <template #default="scope">
                <el-button
                  size="small"
                  type="primary"
                >
                  <span>{{ getStatus(scope.row, 'status') }}</span>
                </el-button>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="160">
                <template #default="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
                <template #default="scope">
                  <el-tooltip content="修改" placement="top">
                    <el-button
                      v-hasPermi="['sys:clue:edit']"
                      link
                      type="primary"
                      icon="Edit"
                      @click="handleUpdate(scope.row)"
                    >编辑</el-button>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                      <el-button
                          v-hasPermi="['sys:clue:remove']"
                          link
                          type="danger"
                          icon="Delete"
                          @click="handleDelete(scope.row)"
                      >删除</el-button>
                  </el-tooltip>
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total > 0"
            v-model:page="queryParams.pageNum"
            v-model:limit="queryParams.pageSize"
            :total="total"
            @pagination="getList"
        />

        <!-- 添加或修改线索配置对话框 -->
        <el-dialog v-model="open" :title="title" width="500px" append-to-body>
            <el-form ref="clueRef" :model="form" :rules="rules" label-width="100px">
              <el-form-item label="线索编号" prop="clueSn">
                <el-input v-model="form.clueSn" placeholder="线索编号自动生成" disabled/>
              </el-form-item>
              <el-form-item label="线索名称" prop="name">
                <el-input v-model="form.name" placeholder="请输入线索名称" />
              </el-form-item>
              <el-form-item label="线索号码" prop="mobile">
                <el-input v-model="form.mobile" placeholder="请输入线索号码" />
              </el-form-item>
              <el-form-item label="线索城市" prop="mobileCity">
                <el-input v-model="form.mobileCity" placeholder="请输入线索城市" disabled/>
              </el-form-item>
              <el-form-item label="线索IP" prop="ipaddr">
                <el-input v-model="form.ipaddr" placeholder="请输入线索IP" />
              </el-form-item>
              <el-form-item label="线索IP地址" prop="ipCity">
                <el-input v-model="form.ipCity" placeholder="请输入线索IP地址" disabled/>
              </el-form-item>
              <el-form-item label="分配用户" prop="userId">
                <el-select v-model="form.userId" clearable placeholder="请选择分配用户">
                  <el-option
                    v-for="(item, index) in userList"
                    :key="index"
                    :label="item.userName"
                    :value="item.userId"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="使用状态">
                <el-radio-group v-model="form.status">
                  <template
                    v-for="dict in sys_clue_status"
                    :key="dict.value"
                  >
                    <el-radio
                      :label="dict.value * 1"
                      :value="dict.value * 1"
                    >{{ dict.label }}</el-radio>
                  </template>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="备注">
                <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
              </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="submitForm">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup name="Clue" lang="ts">
/* eslint-disable camelcase */
import {
  addClue,
  delClue, getClue,
  listClue,
  updateClue,
} from '@/api/clue/index';

import { parseTime } from '@/utils/ruoyi';
import { getCurrentInstance, ComponentInternalInstance, ref, reactive, toRefs, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import {Open} from "@element-plus/icons-vue";
import {listUser} from "@/api/system/user";

const router = useRouter();
const { proxy }:any = getCurrentInstance() as ComponentInternalInstance;

const { sys_clue_status } = proxy!.useDict('sys_clue_status');

const userList = ref<any[]>([]);
const clueList = ref<any[]>([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<number[]>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref('');
const dateRange = ref<any>([]);
const menuExpand = ref(false);
const menuNodeAll = ref(false);
const deptExpand = ref(true);
const deptNodeAll = ref(false);
const menuRef = ref<any>(null);
const deptRef = ref<any>(null);

const data = reactive<{
    form: any;
    queryParams: any;
    rules: any;
}>({
    form: {
      status: undefined
    },
    queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        status: undefined,
    },
    rules: {
        name: [{ required: true, message: '线索名称不能为空', trigger: 'blur' }],
    },
});

const { queryParams, form, rules } = toRefs(data);
/** 获取用户列表数据 */
function getUser(){
  listUser({
    pageNum: 1,
    pageSize: 1000
  }).then((res: any) => {
    userList.value = res.rows;
  });
}
/** 查询线索列表 */
function getList() {
  loading.value = true;
  listClue(proxy!.addDateRange(queryParams.value, dateRange.value)).then((response: any) => {
    clueList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
/** 搜索按钮操作 */
function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
}
/** 重置按钮操作 */
function resetQuery() {
    dateRange.value = [];
    proxy!.resetForm('queryRef');
    handleQuery();
}
/** 删除按钮操作 */
function handleDelete(row: any) {
    const clueIds = row.id || ids.value;
    proxy!.$modal
        .confirm('是否确认删除线索编号为"' + clueIds + '"的数据项?')
        .then(function () {
            return delClue(clueIds);
        })
        .then(() => {
            getList();
            proxy!.$modal.msgSuccess('删除成功');
        })
        .catch((e: any) => {
            console.log(e);
        });
}
/** 导出按钮操作 */
function handleExport() {
    proxy!.download(
        'sys/clue/export',
        {
            ...queryParams.value,
        },
        `role_${new Date().getTime()}.xlsx`
    );
}
/** 多选框选中数据 */
function handleSelectionChange(selection: any[]) {
    ids.value = selection.map(item => item.id);
    single.value = selection.length !== 1;
    multiple.value = !selection.length;
}
/** 获取线索状态 */
function getStatus(row:any, key:string){
  const find = (sys_clue_status.value || []).find(item => item.value * 1 === row[key]) || {}
  return find.label || '暂无状态'
}
/** 线索状态修改 */
function handleStatusChange(row: any) {
    let text = row.status === '0' ? '启用' : '停用';
    proxy!.$modal
        .confirm('确认要"' + text + '""' + row.name + '"线索吗?')
        .then(function () {
            // return changeRoleStatus(row.roleId, row.status);
        })
        .then(() => {
            proxy!.$modal.msgSuccess(text + '成功');
        })
        .catch(function () {
            row.status = row.status === '0' ? '1' : '0';
        });
}
/** 重置新增的表单以及其他数据  */
function reset() {
    if (menuRef.value) {
        menuRef.value.setCheckedKeys([]);
    }
    menuExpand.value = false;
    menuNodeAll.value = false;
    deptExpand.value = true;
    deptNodeAll.value = false;
    form.value = {
        roleId: undefined,
        name: undefined,
        roleKey: undefined,
        roleSort: 0,
        status: '0',
        menuIds: [],
        deptIds: [],
        menuCheckStrictly: true,
        deptCheckStrictly: true,
        remark: undefined,
    };
    proxy!.resetForm('clueRef');
}
/** 添加线索 */
function handleAdd() {
  reset();
  getUser();
  open.value = true;
  title.value = '添加线索';
}
/** 修改线索 */
function handleUpdate(row: any) {
  reset();
  getUser();
  const clueId = row.id || ids.value;
  getClue(clueId).then(response => {
      form.value = response.data;
      nextTick(() => {
        open.value = true;
      });
      title.value = '修改线索';
  });
}
/** 提交按钮 */
function submitForm() {
  (proxy?.$refs['clueRef'] as any).validate((valid: any) => {
    if (valid) {
      if (!form.value.userId) {
        form.value.deptId = ''
      } else {
        const userData = userList.value.find(user => user.userId === form.value.userId)
        if (userData) {
          form.value.deptId = userData.deptId
        }
      }
      if (form.value.id !== undefined) {
        updateClue(form.value).then(response => {
            proxy!.$modal.msgSuccess('修改成功');
            open.value = false;
            getList();
        });
      } else {
        addClue(form.value).then(response => {
            proxy!.$modal.msgSuccess('新增成功');
            open.value = false;
            getList();
        });
      }
    }
  });
}
/** 取消按钮 */
function cancel() {
    open.value = false;
    reset();
}
getList();
</script>
