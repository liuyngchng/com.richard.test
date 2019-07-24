 ## OS X Setup
The following command install all the required tools and libraries to build and install the Apache Thrift compiler on a OS X based system. 
  
### Install Boost
Download the boost library from [boost.org](http://www.boost.org) untar compile with

        ./bootstrap.sh
         sudo ./b2 threading=multi address-model=64 variant=release stage install
 
 ### Install libevent
 Download [libevent](http://monkey.org/~provos/libevent), untar and compile with
 
        ./configure --prefix=/usr/local 
         make
         sudo make install
 
 ### Building Apache Thrift
 Download the latest version of [Apache Thrift](/download), untar and compile with
 
         ./configure --prefix=/usr/local/ --with-boost=/usr/local --with-libevent=/usr/local
 
 ## Additional reading

For more information on the requirements see: [Apache Thrift Requirements](/docs/install)

For more information on building and installing Thrift see: [Building from source](/docs/BuildingFromSource)

## OTHER ERROR ON MAC OS
When execute 'make' in thrift directory ,some error occurred,   
troubleshoot following the steps:
    
    ## can't found header openssl/
    cd /usr/local/Cellar/openssl/1.0.2q/include
    # cd /Applications/homebrew-master/Cellar/openssl/1.0.2r/include
    tar -cf openssl.tar openssl
    tar -xf openssl.tar
    cp openssl.tar ./${thrift_dir} 
    ## composer: No such file or directory
    curl -sS https://getcomposer.org/installer | php
    mv composer.phar /usr/local/bin/composer
    
   
    
    
