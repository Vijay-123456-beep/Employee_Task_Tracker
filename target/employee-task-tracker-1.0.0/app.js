(function(){
  const app = angular.module('taskApp', []);

  app.controller('TaskCtrl', ['$http', '$filter', function($http, $filter){
    const vm = this;
    vm.api = '/api/tasks';
    vm.tasks = [];
    vm.form = { title: '', description: '', assignee: '', dueDate: '', status: 'TODO' };

    vm.load = function(){
      $http.get(vm.api).then(res => {
        vm.tasks = res.data;
      }, err => alert('Failed to load tasks: ' + (err.data && err.data.error || err.status)));
    };

    function formatDateForApi(d){
      if (!d) return null;
      if (typeof d === 'string') return d;
      return $filter('date')(d, 'yyyy-MM-dd');
    }

    vm.createTask = function(){
      const payload = {
        title: vm.form.title,
        description: vm.form.description,
        assignee: vm.form.assignee,
        status: vm.form.status,
        dueDate: formatDateForApi(vm.form.dueDate)
      };
      $http.post(vm.api, payload).then(res => {
        vm.tasks.unshift(res.data);
        vm.form = { title: '', description: '', assignee: '', dueDate: '', status: 'TODO' };
      }, err => alert('Create failed: ' + (err.data && err.data.error || err.status)));
    };

    vm.markDone = function(t){
      const payload = Object.assign({}, t, { status: 'DONE' });
      $http.put(vm.api, payload).then(res => {
        const updated = res.data;
        const idx = vm.tasks.findIndex(x => x.id === updated.id);
        if (idx >= 0) vm.tasks[idx] = updated;
      }, err => alert('Update failed: ' + (err.data && err.data.error || err.status)));
    };

    vm.remove = function(t){
      $http({ method: 'DELETE', url: vm.api + '?id=' + encodeURIComponent(t.id) }).then(() => {
        vm.tasks = vm.tasks.filter(x => x.id !== t.id);
      }, err => alert('Delete failed: ' + (err.data && err.data.error || err.status)));
    };

    vm.load();
  }]);
})();
