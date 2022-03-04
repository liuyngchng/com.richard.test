# 1. healthy check
sudo curl --cacert /etc/elasticsearch/certs/http_ca.crt  'https://127.0.0.1:9200/_cluster/health?pretty' -u elastic

