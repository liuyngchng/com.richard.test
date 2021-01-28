# 1. golang setup

setup golang
```
sudo apt-get install golang
```

setup godep, a go project dependency manage tool

```
sudo apt-get install go-dep
```
or `go get github.com/tools/godep`

# 2. get sourcecode from GitHub

```
mkdir go
cd go
export GOPATH = `pwd`
cd $GOPATH
go get github.com/apache/openwhisk-wskdeploy  # see known issues below if you get an error
go get github.com/tools/godep # get the dependency manager, a godep executable file produced in $gopath/bin

```
# 3. build binary
```
cd src/github.com/apache/openwhisk-wskdeploy/
godep restore
go build -o wskdeploy
```

# 4. troubleshooting
## 4.1 error downloagding dep
```
godep: error downloading dep (golang.org/x/sys/unix): unrecognized import path "golang.org/x/sys/unix"

```
to do 
```
mkdir $GOPATH/src/golang.org/x -p
cd $GOPATH/src/golang.org/x
git clone git@github.com:golang/text.git --depth=1
cd $GOPATH/src/
go install -x golang.org/x/text   //在$GOPATH/pkg目录下生成一个text.a的包文件
```
## 4.2 i/o timeout

```
go: github.com/apache/openwhisk-client-go@v0.0.0-20191018191012-ee5b8709787c: Get https://proxy.golang.org/github.com/apache/openwhisk-client-go/@v/v0.0.0-20191018191012-ee5b8709787c.mod: dial tcp 172.217.27.145:443: i/o timeout

```
to do  
Go1.12及以下：

Bash (Linux or macOS)

```
export GO111MODULE=on
export GOPROXY=https://goproxy.io
```
go1.13以上  
```
go env -w GO111MODULE=on
go env -w GOPROXY=https://goproxy.io,direct

```

