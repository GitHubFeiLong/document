// 接口：是一种能力，一种约束
(function () {
    // 输出姓名
    function showFullName(person) {
        return person.firstName + '_' + person.lastName;
    }
    var person = {
        // firstName: '东方',
        lastName: '不败',
    };
    console.log(showFullName(person));
})();
