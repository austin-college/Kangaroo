<div id="job_${job.id}" class="batchJob">
    <b>${job.name}:</b>
    <span class="status">${job.status()}</span>

    <div class="actionLinks">
        <a href="#" class="runLink" rel="${job.id}">Run</a>
    </div>
</div>
