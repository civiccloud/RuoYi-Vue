import request from '@/utils/request';

// 查询线索列表
export function listClue(query: any) {
  return request({
    url: '/sys/clue/list',
    method: 'get',
    params: query,
  });
}

// 查询线索
export function getClue(query: any) {
  return request({
    url: '/sys/clue/info/' + query,
    method: 'get',
  });
}

// 新增线索
export function addClue(data: any) {
  return request({
    url: '/sys/clue/info',
    method: 'post',
    data: data,
  });
}

// 修改线索
export function updateClue(data: any) {
  return request({
    url: '/sys/clue/info',
    method: 'put',
    data: data,
  });
}

// 删除线索
export function delClue(userId: any) {
  return request({
    url: '/sys/clue/' + userId,
    method: 'delete',
  });
}
