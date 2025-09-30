import os
import torch
import torchrec
import torch.distributed as dist

os.environ["RANK"] = "0"
os.environ["WORLD_SIZE"] = "1"
os.environ["MASTER_ADDR"] = "localhost"
os.environ["MASTER_PORT"] = "29500"

# Note - you will need a V100 or A100 to run tutorial as as!
# If using an older GPU (such as colab free K80),
# you will need to compile fbgemm with the appripriate CUDA architecture
# or run with "gloo" on CPUs
dist.init_process_group(backend="nccl")
