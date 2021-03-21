# TS 使用时报错

##  Property 'xxx' does not exist on type 'typeof import("*.ts")'.

使用ts定义的函数，然后在组件中使用时报错

ts文件定义函数：

```typescript
// 此时暴露出去的就只有一个默认的方法
export default function method(){
    alert('未 做');
}
```

使用方式1：

```typescript
// 引入工具ts，因为只暴露了一个方法，所以直接使用
import method from '/src/utils/notDone.ts'
method();
```

报错：

```bash
 Value of type 'DefineComponent<{}, {}, any, Record<string, ComputedGetter<any> | WritableComputedOptions<any>>, MethodOptions, ... 6 more ..., {}>' is not callable. Did you mean to include 'new'?
```

正确使用方式：

```typescript
(method as any)();
```

使用方式2：

```typescript
<!--ts文件-->
// 此时暴露出去的没有默认的方法
export function method(){
    alert('未 做');
}
export const a = 100;

<!--组件-->
import * as Tools from '/src/utils/notDone.ts'
console.log(Tools.a);
Tools.method()
```

错误信息：

```bash
Property 'a' does not exist on type 'typeof import("*.ts")'.
Property 'method' does not exist on type 'typeof import("*.ts")'.
```

正确使用方式：

```typescript
console.log((Tools as any).a);
(Tools as any).method()
```

