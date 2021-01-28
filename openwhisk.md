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
go get github.com/tools/godep # get the dependency manager

```
# 3. build binary
```
cd src/github.com/apache/openwhisk-wskdeploy/
godep restore
go build -o wskdeploy
```

# 4. troubleshooting

```
godep: error downloading dep (golang.org/x/sys/unix): unrecognized import path "golang.org/x/sys/unix"

```
to do 
```
export GO111MODULE=on
export GOPROXY=https://goproxy.io
mkdir $GOPATH/src/golang.org/x -p
cd $GOPATH/src/golang.org/x
git clone git@github.com:golang/text.git --depth=1
rm -rf text/.git
```


