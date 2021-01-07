# Git常用命令

## 新建仓库

```bash
# 通过 SSH
git clone ssh://user@domain.com/repo.git

#通过 HTTP
git clone http://domain.com/user/repo.git

# 初始化本地仓库
git init
```



## 添加修改

```bash
# 把指定文件添加到暂存区
$ git add xxx

# 把当前所有修改添加到暂存区
$ git add .

# 把所有修改添加到暂存区
$ git add -A
```



## 提交

```bash
# 提交本地的所有修改
$ git commit -a

# 提交之前已标记的变化
$ git commit

# 附加消息提交
$ git commit -m 'commit message'
```



## 储藏

```bash
# 1. 将修改作为当前分支的草稿保存
$ git stash

# 2. 查看草稿列表
$ git stash list
stash@{0}: WIP on master: 6fae349 :memo: Writing docs.

# 3.1 删除草稿
$ git stash drop stash@{0}

# 3.2 读取草稿
$ git stash apply stash@{0}
```



## 撤销修改

```bash
# 移除缓存区的所有文件（i.e. 撤销上次git add）
$ git reset HEAD

# 将HEAD重置到上一次提交的版本，并将之后的修改标记为未添加到缓存区的修改
$ git reset <commit>

# 将HEAD重置到上一次提交的版本，并保留未提交的本地修改
$ git reset --keep <commit>

# 放弃工作目录下的所有修改
$ git reset --hard HEAD

# 将HEAD重置到指定的版本，并抛弃该版本之后的所有修改
$ git reset --hard <commit-hash>

# 用远端分支强制覆盖本地分支
$ git reset --hard <remote/branch> e.g., upstream/master, origin/my-feature

# 放弃某个文件的所有本地修改
$ git checkout HEAD <file>
```

删除添加`.gitignore`文件前错误提交的文件：

```
$ git rm -r --cached .
$ git add .
$ git commit -m "remove xyz file"
```

撤销远程修改（创建一个新的提交，并回滚到指定版本）：

```
$ git revert <commit-hash>
```

彻底删除指定版本：

```
# 执行下面命令后，commit-hash 提交后的记录都会被彻底删除，使用需谨慎
$ git reset --hard <commit-hash>
$ git push -f
```

### 

### 更新与推送

更新：

```bash
# 下载远程端版本，但不合并到HEAD中
$ git fetch <remote>

# 将远程端版本合并到本地版本中
$ git pull origin master

# 以rebase方式将远端分支与本地合并
$ git pull --rebase <remote> <branch>
```

推送：

```bash
# 将本地版本推送到远程端
$ git push remote <remote> <branch>

# 删除远程端分支
$ git push <remote> :<branch> (since Git v1.5.0)
$ git push <remote> --delete <branch> (since Git v1.7.0)

# 发布标签
$ git push --tags
```

### 

### 查看信息

显示工作路径下已修改的文件：

```bash
$ git status
```

显示与上次提交版本文件的不同：

```bash
$ git diff
```

显示提交历史：

```bash
# 从最新提交开始，显示所有的提交记录（显示hash， 作者信息，提交的标题和时间）
$ git log

# 显示某个用户的所有提交
$ git log --author="username"

# 显示某个文件的所有修改
$ git log -p <file>
```

显示搜索内容：

```bash
# 从当前目录的所有文件中查找文本内容
$ git grep "Hello"

# 在某一版本中搜索文本
$ git grep "Hello" v2.5
```

### 分支

增删查分支：

```bash
# 列出所有的分支
$ git branch -a

# 列出所有的远端分支
$ git branch -r

# 基于当前分支创建新分支
$ git branch new-branch

# 基于远程分支创建新的可追溯的分支
$ git branch --track new-branch remote-branch

# 将本地分支push到github
git push origin new-branch

# 删除github上的分支
$ git push origin :new-branch

# 删除本地分支
$ git branch -d branch

# 强制删除本地分支，将会丢失未合并的修改
$ git branch -D branch
```

切换分支：

```bash
# 切换分支
$ git checkout branch

# 创建并切换到新分支
$ git checkout -b branch
```



### 标签

```bash
# 给当前版本打标签
$ git tag tag-name

# 给当前版本打标签并附加消息
$ git tag -a tag-name
```

### 合并与变基

> merge 与 rebase 虽然是 git 常用功能，但是强烈建议不要使用 git 命令来完成这项工作。
>
> 因为如果出现代码冲突，在没有代码比对工具的情况下，实在太艰难了。
>
> 你可以考虑使用各种 Git GUI 工具。

合并：

```bash
# 将分支合并到当前HEAD中
$ git merge <branch>

# 合并时提示 can not merge
## 先将代码储藏 注：此命令是备份当前的工作区，防止当前工程中已修改的代码出现丢失，同时将工作区中的代码保存到git栈中。
git stash
## 再执行命令：拉取更新 注：此命令是不再解释。
git pull
## 然后执行命令：注：此命令是从git栈中读取命令1保存的内容，恢复工作区
git stash pop
## 最后合并分支
git merge branch
```

变基：

​		使用 rebase 可以使git历史变为完整一条线

```bash
# 将 <branch> 的分支变基到当前分支
$ git rebase <branch>
```



## 将分支合并成一条线

1. 先找到出现分叉前的commit ID，复制下来，然后使用 `reset` 命令，将当前分支回退到指定commitID分支。

   ```bash
   $ git reset 974a75a7
   ```

2. 使用上面的命令后，本地更改的代码不会消失，只会保存在暂存区。此时在正常添加到工作区，然后提交。

## 添加远程仓库

1. 初始化本地git

   ```bash
   $ git init
   ```

2. 添加远程仓库

   ```bash
   $ git remote add origin https://github.com/GitHubFeiLong/spring-security.git
   ```

3. 如果本地有文件的话，先提交，push，然后再pull

   ```bash
   $ git  add .
   $ git commit -m"init"
   $ git push
   $ git pull
   ```

   

## 仓库迁移

本地分支 拉取新的远程分支时报错：`fatal: refusing to merge unrelated histories` 在后部添加 `--allow-unrelated-histories` 完美解决

```bash
git pull origin master --allow-unrelated-histories
```

